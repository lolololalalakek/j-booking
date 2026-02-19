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
import uz.stajirovka.jbooking.entity.CityEntity;
import uz.stajirovka.jbooking.entity.GuestEntity;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.exception.ConflictException;
import uz.stajirovka.jbooking.exception.InternalException;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.exception.ValidationException;
import uz.stajirovka.jbooking.mapper.BookingMapper;
import uz.stajirovka.jbooking.repository.BookingRepository;
import uz.stajirovka.jbooking.repository.CityRepository;
import uz.stajirovka.jbooking.repository.HotelRepository;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.service.BookingPriceService;
import uz.stajirovka.jbooking.service.BookingService;
import uz.stajirovka.jbooking.service.CurrencyConverterService;
import uz.stajirovka.jbooking.service.GuestService;
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
    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;
    private final GuestService guestService;
    private final BookingMapper bookingMapper;
    private final BookingPriceService bookingPriceService;
    private final BookingProperties bookingProperties;
    private final PaymentExecutor paymentExecutor;
    private final PaymentProcessingService paymentProcessingService;
    private final CurrencyConverterService currencyConverter;

    // создание нового бронирования
    @Override
    @Transactional
    public BookingResponse initBooking(BookingCreateRequest request) {
        long nights = BookingDateValidator.validateAndCalculateNights(
                request.checkInDate(), request.checkOutDate(), bookingProperties.getMinNights());

        // блокируем комнату для предотвращения race condition
        RoomEntity room = findRoomWithLock(request.cityId(), request.hotelId(), request.roomId());

        // проверка доступности номера на выбранные даты (сразу после блокировки)
        if (bookingRepository.existsOverlappingBooking(
                request.roomId(), request.checkInDate(), request.checkOutDate(), BookingStatus.CANCELLED)) {
            throw new ConflictException(Error.ROOM_NOT_AVAILABLE);
        }

        // считаем общее количество гостей
        int totalGuests = bookingProperties.getMainGuestCount();
        if (request.additionalGuests() != null) {
            totalGuests += request.additionalGuests().size();
        }

        // проверка вместимости
        if (totalGuests > room.getCapacity()) {
            throw new ValidationException(Error.VALIDATION_ERROR,
                    "Количество гостей (" + totalGuests + ") превышает вместимость номера (" + room.getCapacity() + ")");
        }

        // находим или создаём основного гостя
        GuestEntity mainGuest = guestService.findOrCreateGuest(request.mainGuest());

        // находим или создаём дополнительных гостей
        Set<GuestEntity> additionalGuests = new HashSet<>();
        if (request.additionalGuests() != null) {
            for (GuestInfoRequest guestInfo : request.additionalGuests()) {
                additionalGuests.add(guestService.findOrCreateGuest(guestInfo));
            }
        }

        // находим отель и город для бронирования
        HotelEntity hotel = hotelRepository.findByRoomId(room.getId())
                .orElseThrow(() -> new NotFoundException(Error.HOTEL_NOT_FOUND, "roomId=" + room.getId()));
        CityEntity city = cityRepository.findByHotelId(hotel.getId())
                .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND, "hotelId=" + hotel.getId()));

        // создаём бронирование
        BookingEntity entity = new BookingEntity();
        entity.setCity(city);
        entity.setHotel(hotel);
        entity.setRoom(room);
        entity.setGuest(mainGuest);
        entity.setAdditionalGuests(additionalGuests);
        entity.setCheckInDate(request.checkInDate());
        entity.setCheckOutDate(request.checkOutDate());
        entity.setStatus(BookingStatus.HOLD);

        // фиксируем цену за ночь (snapshot) и рассчитываем итого
        entity.setPricePerNight(room.getPricePerNight());
        entity.setTotalPrice(bookingPriceService.calculate(room, nights));

        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return bookingMapper.toResponse(bookingRepository.save(entity));
    }

    // confirmBooking НЕ @Transactional — оркестрирует 3 фазы
    @Override
    public BookingConfirmResponse confirmBooking(BookingConfirmRequest request) {
        Long bookingId = request.bookingId();

        // фаза 1: короткая транзакция — lock, валидация, HOLD -> PAYMENT_PROCESSING
        PaymentRequest paymentRequest = paymentProcessingService.validateAndLock(bookingId, request);

        // HOLD просрочен — фаза 1 уже поставила CANCELLED
        if (paymentRequest == null) {
            return new BookingConfirmResponse(BookingStatus.CANCELLED, bookingId, null);
        }

        // Фаза 2: вызов платёжки БЕЗ транзакции — lock уже отпущен
        PaymentResponse paymentResponse;
        try {
            paymentResponse = paymentExecutor.executePayment(paymentRequest);
        } catch (Exception e) {
            log.error("Ошибка платёжного сервиса для bookingId={}: {}", bookingId, e.getMessage());
            throw new InternalException(Error.INTERNAL_ERROR, "Ошибка платёжного сервиса: " + e.getMessage());
        }

        // Фаза 3: короткая транзакция — сохраняем результат платежа
        BookingStatus finalStatus = paymentProcessingService.savePaymentResult(bookingId, paymentResponse);

        return new BookingConfirmResponse(finalStatus, bookingId, paymentResponse.id());
    }

    @Override
    public BookingResponse getById(Long id, Currency currency) {
        BookingResponse response = bookingMapper.toResponse(findById(id));
        return convertPrice(response, currency);
    }

    @Override
    public Slice<BookingResponse> getAllByPinfl(String pinfl, Currency currency, Pageable pageable) {
        return bookingRepository.findByGuestPinfl(pinfl, pageable)
                .map(bookingMapper::toResponse)
                .map(r -> convertPrice(r, currency));
    }

    private BookingResponse convertPrice(BookingResponse response, Currency currency) {
        return new BookingResponse(
                response.id(),
                response.cityId(),
                response.cityName(),
                response.hotelId(),
                response.hotelName(),
                response.roomId(),
                response.roomNumber(),
                response.mainGuest(),
                response.additionalGuests(),
                response.totalGuests(),
                response.checkInDate(),
                response.checkOutDate(),
                response.status(),
                currencyConverter.convert(response.pricePerNight(), currency),
                currencyConverter.convert(response.totalPrice(), currency),
                response.createdAt(),
                response.updatedAt()
        );
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
