package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.dto.request.BookingCreateRequest;
import uz.stajirovka.jbooking.dto.response.BookingResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.exception.ResourceNotFoundException;
import uz.stajirovka.jbooking.mapper.BookingMapper;
import uz.stajirovka.jbooking.repository.BookingRepository;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.service.BookingService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final BookingMapper bookingMapper;

    // создание нового бронирования
    @Override
    @Transactional
    public BookingResponse create(BookingCreateRequest request) {
        if (!request.checkOutDate().isAfter(request.checkInDate())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        RoomEntity room = findRoomById(request.roomId());

        if (request.numberOfGuests() > room.getCapacity()) {
            throw new IllegalArgumentException(
                "Number of guests (" + request.numberOfGuests() +
                    ") exceeds room capacity (" + room.getCapacity() + ")");
        }

        BookingEntity entity = bookingMapper.toEntity(request);
        entity.setRoom(room);
        entity.setStatus(BookingStatus.HOLD);

        long nights = ChronoUnit.DAYS.between(
            request.checkInDate().toLocalDate(),
            request.checkOutDate().toLocalDate());
        entity.setTotalPrice(room.getPricePerNight().multiply(BigDecimal.valueOf(nights)));

        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return bookingMapper.toResponse(bookingRepository.save(entity));
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

    // подтверждение бронирования
    @Override
    @Transactional
    public BookingResponse confirm(Long id) {
        BookingEntity entity = findById(id);

        if (entity.getStatus() != BookingStatus.HOLD) {
            throw new IllegalArgumentException(
                "Can only confirm bookings in HOLD status, current: " + entity.getStatus());
        }

        entity.setStatus(BookingStatus.CONFIRMED);
        entity.setUpdatedAt(LocalDateTime.now());
        return bookingMapper.toResponse(entity);
    }

    // отмена бронирования
    @Override
    @Transactional
    public BookingResponse cancel(Long id) {
        BookingEntity entity = findById(id);

        if (entity.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("Booking is already cancelled");
        }

        entity.setStatus(BookingStatus.CANCELLED);
        entity.setUpdatedAt(LocalDateTime.now());
        return bookingMapper.toResponse(entity);
    }

    // удаление бронирования по идентификатору
    @Override
    @Transactional
    public void delete(Long id) {
        findById(id);
        bookingRepository.deleteById(id);
    }

    // поиск бронирования по идентификатору или выброс исключения
    private BookingEntity findById(Long id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking", id));
    }

    // поиск комнаты по идентификатору или выброс исключения
    private RoomEntity findRoomById(Long id) {
        return roomRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Room", id));
    }
}
