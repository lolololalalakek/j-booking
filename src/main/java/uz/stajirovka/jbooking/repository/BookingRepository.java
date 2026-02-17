package uz.stajirovka.jbooking.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.entity.BookingEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BookingEntity b WHERE b.id = :id")
    Optional<BookingEntity> findByIdWithLock(@Param("id") Long id);

    // получение бронирований гостя по ПИНФЛ
    @Query("""
        SELECT b FROM BookingEntity b
        JOIN FETCH b.room
        JOIN FETCH b.hotel
        JOIN FETCH b.city
        JOIN FETCH b.guest
        WHERE b.guest.pinfl = :pinfl
        """)
    Slice<BookingEntity> findByGuestPinfl(@Param("pinfl") String pinfl, Pageable pageable);

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

    // поиск просроченных бронирований в статусе HOLD (батчами)
    @Query("""
        SELECT b FROM BookingEntity b
        WHERE b.status = :holdStatus
          AND b.createdAt < :expiredBefore
        """)
    Slice<BookingEntity> findExpiredHolds(
            @Param("holdStatus") BookingStatus holdStatus,
            @Param("expiredBefore") LocalDateTime expiredBefore,
            Pageable pageable
    );
}
