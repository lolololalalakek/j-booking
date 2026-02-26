package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.component.properties.BookingProperties;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.BookingConfirmRequest;
import uz.stajirovka.jbooking.dto.request.BookingCreateRequest;
import uz.stajirovka.jbooking.dto.request.GuestInfoRequest;
import uz.stajirovka.jbooking.dto.request.PaymentRequest;
import uz.stajirovka.jbooking.dto.response.BookingConfirmResponse;
import uz.stajirovka.jbooking.dto.response.BookingResponse;
import uz.stajirovka.jbooking.dto.response.PaymentResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.GuestEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.exception.ConflictException;
import uz.stajirovka.jbooking.exception.InternalException;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.exception.ValidationException;
import uz.stajirovka.jbooking.mapper.BookingMapper;
import uz.stajirovka.jbooking.repository.BookingRepository;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.service.BookingPriceService;
import uz.stajirovka.jbooking.service.BookingService;
import uz.stajirovka.jbooking.service.CurrencyConverterService;
import uz.stajirovka.jbooking.service.GuestService;
import uz.stajirovka.jbooking.service.NotificationProcessorService;
import uz.stajirovka.jbooking.service.PaymentExecutor;
import uz.stajirovka.jbooking.utility.BookingDateValidator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final GuestService guestService;
    private final BookingMapper bookingMapper;
    private final BookingPriceService bookingPriceService;
    private final CurrencyConverterService currencyConverterService;
    private final BookingProperties bookingProperties;
    private final PaymentExecutor paymentExecutor;
    private final PaymentProcessingService paymentProcessingService;
    private final NotificationProcessorService notificationProcessorService;

    // создание нового бронирования
    @Override
    @Transactional
    public BookingResponse initBooking(BookingCreateRequest request) {
        long nights = BookingDateValidator.validateAndCalculateNights(
            request.checkInDate(), request.checkOutDate(), bookingProperties.getMinNights());

        // сразу берём lock — другая транзакция не займёт комнату пока мы работаем
        // hotel и city загружены через JOIN FETCH в lock-запросе
        RoomEntity room = findRoomWithLock(request.cityId(), request.hotelId(), request.roomId());

        // проверка доступности под lock
        if (bookingRepository.existsOverlappingBooking(
            request.roomId(), request.checkInDate(), request.checkOutDate(), BookingStatus.CANCELLED)) {
            throw new ConflictException(Error.ROOM_NOT_AVAILABLE);
        }

        // проверка вместимости под lock
        int totalGuests = 1 + (request.additionalGuests() != null ? request.additionalGuests().size() : 0);
        if (totalGuests > room.getCapacity()) {
            throw new ValidationException(Error.VALIDATION_ERROR,
                "Количество гостей (" + totalGuests + ") превышает вместимость номера (" + room.getCapacity() + ")");
        }

        // создаём гостей только после того как убедились что комната свободна
        GuestEntity mainGuest = guestService.findOrCreateGuest(request.mainGuest());

        Set<GuestEntity> additionalGuests = new HashSet<>();
        if (request.additionalGuests() != null) {
            for (GuestInfoRequest guestInfo : request.additionalGuests()) {
                additionalGuests.add(guestService.findOrCreateGuest(guestInfo));
            }
        }

        return bookingMapper.toResponse(
            bookingRepository.save(buildBooking(request, room, mainGuest, additionalGuests, nights)));
    }

    // confirmBooking БЕЗ @Transactional — оркестрирует 3 фазы
    // внешний вызов платёжки не должен держать DB-соединение открытым
    @Override
    public BookingConfirmResponse confirmBooking(BookingConfirmRequest request) {
        Long bookingId = request.bookingId();

        // фаза 1: короткая транзакция — lock, валидация, HOLD -> PAYMENT_PROCESSING
        PaymentRequest paymentRequest = paymentProcessingService.validateAndLock(bookingId, request);

        // HOLD просрочен — фаза 1 уже поставила CANCELLED
        if (paymentRequest == null) {
            return new BookingConfirmResponse(BookingStatus.CANCELLED, bookingId, null);
        }

        // фаза 2: вызов платёжки БЕЗ транзакции — connection pool свободен
        PaymentResponse paymentResponse;
        try {
            paymentResponse = paymentExecutor.executePayment(paymentRequest);
        } catch (Exception e) {
            log.error("Ошибка платёжного сервиса для bookingId={}: {}", bookingId, e.getMessage());
            throw new InternalException(Error.INTERNAL_ERROR, "Ошибка платёжного сервиса: " + e.getMessage());
        }

        // фаза 3: короткая транзакция — сохраняем результат, получаем актуальную сущность
        BookingEntity updated = paymentProcessingService.savePaymentResult(bookingId, paymentResponse);

        // уведомление только если платёж прошёл успешно
        if (updated.getStatus() == BookingStatus.CONFIRMED) {
            notificationProcessorService.process(updated);
        }

        return new BookingConfirmResponse(updated.getStatus(), bookingId, paymentResponse.id());
    }


    @Override
    public BookingResponse getById(Long id, Currency currency) {
        return bookingMapper.withConvertedPrices(bookingMapper.toResponse(findById(id)), currency, currencyConverterService);
    }

    @Override
    public Slice<BookingResponse> getAllByPinfl(String pinfl, Currency currency, Pageable pageable) {
        return bookingRepository.findByGuestPinfl(pinfl, pageable)
            .map(bookingMapper::toResponse)
            .map(r -> bookingMapper.withConvertedPrices(r, currency, currencyConverterService));
    }

    private BookingEntity buildBooking(BookingCreateRequest request, RoomEntity room,
                                       GuestEntity mainGuest, Set<GuestEntity> additionalGuests,
                                       long nights) {
        LocalDateTime now = LocalDateTime.now();
        return BookingEntity.builder()
            .city(room.getHotel().getCity())
            .hotel(room.getHotel())
            .room(room)
            .mainGuest(mainGuest)
            .additionalGuests(additionalGuests)
            .checkInDate(request.checkInDate())
            .checkOutDate(request.checkOutDate())
            .status(BookingStatus.HOLD)
            .pricePerNight(room.getPricePerNight())
            .totalPrice(bookingPriceService.calculate(room, nights))
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    // поиск бронирования по идентификатору или выброс исключения
    private BookingEntity findById(Long id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Error.BOOKING_NOT_FOUND, "id=" + id));
    }

    // поиск комнаты по цепочке город -> отель -> комната с блокировкой (pessimistic lock)
    private RoomEntity findRoomWithLock(Long cityId, Long hotelId, Long roomId) {
        return roomRepository.findByIdAndHotelIdAndCityIdWithLock(roomId, hotelId, cityId)
            .orElseThrow(() -> new NotFoundException(Error.ROOM_NOT_FOUND,
                "cityId=" + cityId + ", hotelId=" + hotelId + ", roomId=" + roomId));
    }
}
