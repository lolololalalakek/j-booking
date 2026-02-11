package uz.stajirovka.jbooking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.entity.BookingEntity;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    // fetch join для ManyToOne связей (additionalGuests загружается через @BatchSize)
    @Query("""
        SELECT b FROM BookingEntity b
        JOIN FETCH b.room r
        JOIN FETCH r.hotel
        JOIN FETCH b.guest
        """)
    Slice<BookingEntity> findAllBy(Pageable pageable);

    // fetch join для ManyToOne связей (additionalGuests загружается через @BatchSize)
    @Query("""
        SELECT b FROM BookingEntity b
        JOIN FETCH b.room r
        JOIN FETCH r.hotel
        JOIN FETCH b.guest
        WHERE b.guest.id = :guestId
        """)
    Slice<BookingEntity> findByGuestId(@Param("guestId") Long guestId, Pageable pageable);

    // проверка пересечения дат бронирования для номера
    @Query("""
        SELECT COUNT(b) > 0 FROM BookingEntity b
        WHERE b.room.id = :roomId
          AND b.status <> :cancelledStatus
          AND b.checkInDate < :checkOutDate
          AND b.checkOutDate > :checkInDate
        """)
    boolean existsOverlappingBooking(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDateTime checkInDate,
            @Param("checkOutDate") LocalDateTime checkOutDate,
            @Param("cancelledStatus") BookingStatus cancelledStatus
    );

    // проверка пересечения дат исключая указанное бронирование (для modify)
    @Query("""
        SELECT COUNT(b) > 0 FROM BookingEntity b
        WHERE b.room.id = :roomId
          AND b.id <> :excludeBookingId
          AND b.status <> :cancelledStatus
          AND b.checkInDate < :checkOutDate
          AND b.checkOutDate > :checkInDate
        """)
    boolean existsOverlappingBookingExcluding(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDateTime checkInDate,
            @Param("checkOutDate") LocalDateTime checkOutDate,
            @Param("cancelledStatus") BookingStatus cancelledStatus,
            @Param("excludeBookingId") Long excludeBookingId
    );

    // проверка наличия активных бронирований для номера (не отменённых и не завершённых)
    @Query("""
        SELECT COUNT(b) > 0 FROM BookingEntity b
        WHERE b.room.id = :roomId
          AND b.status <> :cancelledStatus
          AND b.checkOutDate > :now
        """)
    boolean existsActiveByRoomId(
            @Param("roomId") Long roomId,
            @Param("cancelledStatus") BookingStatus cancelledStatus,
            @Param("now") LocalDateTime now
    );
}
