package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.component.properties.BookingProperties;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.BookingModifyRequest;
import uz.stajirovka.jbooking.dto.response.BookingResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.exception.ConflictException;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.exception.ValidationException;
import uz.stajirovka.jbooking.mapper.BookingMapper;
import uz.stajirovka.jbooking.repository.BookingRepository;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.service.BookingPriceService;
import uz.stajirovka.jbooking.service.BookingStatusService;
import uz.stajirovka.jbooking.service.CancellationPolicyService;

import java.time.temporal.ChronoUnit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingStatusServiceImpl implements BookingStatusService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final BookingMapper bookingMapper;
    private final CancellationPolicyService cancellationPolicyService;
    private final BookingPriceService bookingPriceService;
    private final BookingProperties bookingProperties;

    // допустимые переходы статусов
    private static final Set<BookingStatus> CONFIRMABLE_STATUSES = Set.of(BookingStatus.HOLD, BookingStatus.MODIFIED);
    private static final Set<BookingStatus> PAYABLE_STATUSES = Set.of(BookingStatus.CONFIRMED);
    private static final Set<BookingStatus> CANCELLABLE_STATUSES = Set.of(
            BookingStatus.HOLD, BookingStatus.CONFIRMED, BookingStatus.PAID, BookingStatus.MODIFIED
    );
    private static final Set<BookingStatus> MODIFIABLE_STATUSES = Set.of(
            BookingStatus.CONFIRMED, BookingStatus.PAID
    );




    @Override
    @Transactional
    public BookingResponse cancel(Long bookingId) {
        BookingEntity booking = findById(bookingId);
        validateTransition(booking, CANCELLABLE_STATUSES, BookingStatus.CANCELLED);

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());

        return bookingMapper.toResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse modify(Long bookingId, BookingModifyRequest request) {
        BookingEntity booking = findById(bookingId);
        validateTransition(booking, MODIFIABLE_STATUSES, BookingStatus.MODIFIED);

        // валидация дат
        if (!request.checkOutDate().isAfter(request.checkInDate())) {
            throw new ValidationException(Error.VALIDATION_ERROR, "Дата выезда должна быть после даты заезда");
        }

        long nights = ChronoUnit.DAYS.between(
                request.checkInDate().toLocalDate(),
                request.checkOutDate().toLocalDate());
        if (nights < bookingProperties.getMinNights()) {
            throw new ValidationException(Error.VALIDATION_ERROR,
                    "Минимальный срок проживания — " + bookingProperties.getMinNights() + " ночь");
        }

        // получаем новый номер (может быть тот же или другой)
        RoomEntity newRoom = findRoomById(request.roomId());

        // проверяем вместимость нового номера
        int totalGuests = bookingProperties.getMainGuestCount()
                + (booking.getAdditionalGuests() != null ? booking.getAdditionalGuests().size() : 0);
        if (totalGuests > newRoom.getCapacity()) {
            throw new ConflictException(Error.ROOM_CAPACITY_EXCEEDED,
                    "Гостей: " + totalGuests + ", вместимость: " + newRoom.getCapacity());
        }

        // проверяем доступность нового номера (исключая текущее бронирование)
        boolean hasOverlap = bookingRepository.existsOverlappingBookingExcluding(
                request.roomId(),
                request.checkInDate(),
                request.checkOutDate(),
                BookingStatus.CANCELLED,
                bookingId
        );
        if (hasOverlap) {
            throw new ConflictException(Error.ROOM_NOT_AVAILABLE);
        }

        // обновляем бронирование
        booking.setRoom(newRoom);
        booking.setCheckInDate(request.checkInDate());
        booking.setCheckOutDate(request.checkOutDate());
        booking.setTotalPrice(bookingPriceService.calculate(newRoom, nights));
        booking.setStatus(BookingStatus.MODIFIED);
        booking.setUpdatedAt(LocalDateTime.now());

        return bookingMapper.toResponse(booking);
    }

    @Override
    public BigDecimal getRefundAmount(Long bookingId) {
        BookingEntity booking = findById(bookingId);
        return cancellationPolicyService.calculateRefund(booking);
    }

    @Override
    public int getRefundPercent(Long bookingId) {
        BookingEntity booking = findById(bookingId);
        return cancellationPolicyService.getRefundPercent(booking.getCheckInDate());
    }

    // поиск бронирования по id
    private BookingEntity findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.BOOKING_NOT_FOUND, "id=" + id));
    }

    // поиск номера по id
    private RoomEntity findRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.ROOM_NOT_FOUND, "id=" + id));
    }

    // валидация перехода статуса
    private void validateTransition(BookingEntity booking, Set<BookingStatus> allowedStatuses, BookingStatus targetStatus) {
        if (!allowedStatuses.contains(booking.getStatus())) {
            throw new ConflictException(Error.INVALID_BOOKING_STATUS,
                    "Переход " + booking.getStatus() + " -> " + targetStatus + " недопустим");
        }
    }
}
