package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.component.properties.BookingProperties;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.BookingConfirmRequest;
import uz.stajirovka.jbooking.dto.request.BookingCreateRequest;
import uz.stajirovka.jbooking.dto.request.GuestInfoRequest;
import uz.stajirovka.jbooking.dto.response.BookingConfirmResponse;
import uz.stajirovka.jbooking.dto.response.BookingResponse;
import uz.stajirovka.jbooking.dto.response.PaymentResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.GuestEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.exception.ConflictException;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.exception.ValidationException;
import uz.stajirovka.jbooking.mapper.BookingMapper;
import uz.stajirovka.jbooking.repository.BookingRepository;
import uz.stajirovka.jbooking.repository.GuestRepository;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.service.BookingPriceService;
import uz.stajirovka.jbooking.service.BookingService;
import uz.stajirovka.jbooking.service.PaymentExecutor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final BookingMapper bookingMapper;
    private final BookingPriceService bookingPriceService;
    private final BookingProperties bookingProperties;
    private final PaymentExecutor  paymentExecutor;

    // создание нового бронирования
    @Override
    @Transactional
    public BookingResponse initBooking(BookingCreateRequest request) {
        // валидация дат
        if (!request.checkOutDate().isAfter(request.checkInDate())) {
            throw new ValidationException(Error.VALIDATION_ERROR, "Дата выезда должна быть после даты заезда");
        }

        // проверка минимального срока проживания
        long nights = ChronoUnit.DAYS.between(
            request.checkInDate().toLocalDate(),
            request.checkOutDate().toLocalDate());
        if (nights < bookingProperties.getMinNights()) {
            throw new ValidationException(Error.VALIDATION_ERROR,
                    "Минимальный срок проживания — " + bookingProperties.getMinNights() + " ночь");
        }

        // блокируем комнату для предотвращения race condition
        RoomEntity room = findRoomByIdWithLock(request.roomId());

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
        GuestEntity mainGuest = findOrCreateGuest(request.mainGuest());

        // находим или создаём дополнительных гостей
        Set<GuestEntity> additionalGuests = new HashSet<>();
        if (request.additionalGuests() != null) {
            for (GuestInfoRequest guestInfo : request.additionalGuests()) {
                additionalGuests.add(findOrCreateGuest(guestInfo));
            }
        }

        // создаём бронирование
        BookingEntity entity = new BookingEntity();
        entity.setRoom(room);
        entity.setGuest(mainGuest);
        entity.setAdditionalGuests(additionalGuests);
        entity.setCheckInDate(request.checkInDate());
        entity.setCheckOutDate(request.checkOutDate());
        entity.setStatus(BookingStatus.HOLD);

        // рассчитываем цену
        entity.setTotalPrice(bookingPriceService.calculate(room, nights));

        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return bookingMapper.toResponse(bookingRepository.save(entity));
    }

    @Override
    public BookingConfirmResponse confirmBooking(BookingConfirmRequest request) {
        Long bookingId = request.bookingId();

        BookingEntity bookingEntity = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new NotFoundException(Error.BOOKING_NOT_FOUND));


        BigDecimal totalPrice = bookingEntity.getTotalPrice();

        PaymentResponse paymentResponse = paymentExecutor.executePayment(totalPrice, request.cardId());

        BookingStatus bookingStatus = BookingStatus.fromTransactionStatus(paymentResponse.transactionStatus());

        return new BookingConfirmResponse(bookingStatus, bookingId, paymentResponse.transactionId() );

    }

    // получение бронирования по идентификатору
    @Override
    public BookingResponse getById(Long id) {
        return bookingMapper.toResponse(findById(id));
    }

    // получение всех бронирований с пагинацией
    @Override
    public Slice<BookingResponse> getAll(Pageable pageable) {
        return bookingRepository.findAllBy(pageable)
            .map(bookingMapper::toResponse);
    }


    // находим существующего гостя по email или создаём нового
    // используем pessimistic lock для предотвращения race condition
    private GuestEntity findOrCreateGuest(GuestInfoRequest guestInfo) {
        // если есть email - ищем существующего гостя с блокировкой
        if (guestInfo.email() != null && !guestInfo.email().isBlank()) {
            return guestRepository.findByEmailWithLock(guestInfo.email())
                .orElseGet(() -> createGuest(guestInfo));
        }
        // если email нет - всегда создаём нового
        return createGuest(guestInfo);
    }

    // создание нового гостя
    private GuestEntity createGuest(GuestInfoRequest guestInfo) {
        GuestEntity guest = new GuestEntity();
        guest.setFirstName(guestInfo.firstName());
        guest.setLastName(guestInfo.lastName());
        // пустой email нормализуем в null для корректной работы UNIQUE
        String email = guestInfo.email();
        guest.setEmail(email != null && !email.isBlank() ? email : null);
        guest.setPhone(guestInfo.phone());
        guest.setCreatedAt(LocalDateTime.now());
        return guestRepository.save(guest);
    }

    // получение истории бронирований по идентификатору гостя
    @Override
    public Slice<BookingResponse> getByGuestId(Long guestId, Pageable pageable) {
        return bookingRepository.findByGuestId(guestId, pageable)
                .map(bookingMapper::toResponse);
    }

    // поиск бронирования по идентификатору или выброс исключения
    private BookingEntity findById(Long id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Error.BOOKING_NOT_FOUND, "id=" + id));
    }

    // поиск комнаты с блокировкой (pessimistic lock)
    private RoomEntity findRoomByIdWithLock(Long id) {
        return roomRepository.findByIdWithLock(id)
            .orElseThrow(() -> new NotFoundException(Error.ROOM_NOT_FOUND, "id=" + id));
    }
}
