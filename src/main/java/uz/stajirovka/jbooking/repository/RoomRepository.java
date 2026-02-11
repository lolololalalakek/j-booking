package uz.stajirovka.jbooking.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.stajirovka.jbooking.entity.RoomEntity;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    // fetch join для ManyToOne (amenities загружается через @BatchSize)
    @Query("SELECT r FROM RoomEntity r JOIN FETCH r.hotel")
    Slice<RoomEntity> findAllBy(Pageable pageable);

    // fetch join для ManyToOne (amenities загружается через @BatchSize)
    @Query("SELECT r FROM RoomEntity r JOIN FETCH r.hotel WHERE r.hotel.id = :hotelId")
    Slice<RoomEntity> findByHotelId(@Param("hotelId") Long hotelId, Pageable pageable);

    // блокировка комнаты для предотвращения race condition при бронировании
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RoomEntity r WHERE r.id = :id")
    Optional<RoomEntity> findByIdWithLock(@Param("id") Long id);

    // проверка наличия активных номеров в отеле
    boolean existsByHotelId(Long hotelId);
}
