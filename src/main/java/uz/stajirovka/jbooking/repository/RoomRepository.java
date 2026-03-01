package uz.stajirovka.jbooking.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.stajirovka.jbooking.entity.RoomEntity;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<RoomEntity, Long>, JpaSpecificationExecutor<RoomEntity> {

    @Query("SELECT r FROM HotelEntity h JOIN h.rooms r WHERE h.id = :hotelId AND r.deletedAt IS NULL")
    Slice<RoomEntity> findByHotelId(@Param("hotelId") Long hotelId, Pageable pageable);

    Slice<RoomEntity> findByHotel_IdAndHotel_City_Id(Long hotelId, Long hotelCityId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT r FROM RoomEntity r
        JOIN FETCH r.hotel h
        JOIN FETCH h.city c
        WHERE r.id = :id AND h.id = :hotelId AND c.id = :cityId
        """)
    Optional<RoomEntity> findByIdAndHotelIdAndCityIdWithLock(
        @Param("id") Long id,
        @Param("hotelId") Long hotelId,
        @Param("cityId") Long cityId
    );
}
