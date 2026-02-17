package uz.stajirovka.jbooking.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.entity.RoomEntity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    @Query("SELECT r FROM HotelEntity h JOIN h.rooms r WHERE h.id = :hotelId AND r.deletedAt IS NULL")
    Slice<RoomEntity> findByHotelId(@Param("hotelId") Long hotelId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RoomEntity r WHERE r.id = :id")
    Optional<RoomEntity> findByIdWithLock(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT r FROM CityEntity c
            JOIN c.hotels h
            JOIN h.rooms r
            WHERE r.id = :id AND h.id = :hotelId AND c.id = :cityId
            """)
    Optional<RoomEntity> findByIdAndHotelIdAndCityIdWithLock(
            @Param("id") Long id,
            @Param("hotelId") Long hotelId,
            @Param("cityId") Long cityId
    );

    @Query("""
            SELECT r FROM HotelEntity h
            JOIN h.rooms r
            LEFT JOIN CityEntity c ON h MEMBER OF c.hotels
            WHERE r.deletedAt IS NULL
            AND r.capacity >= :guests
            AND (:cityId IS NULL OR c.id = :cityId)
            AND (:hotelId IS NULL OR h.id = :hotelId)
            AND (:minPrice IS NULL OR r.pricePerNight >= :minPrice)
            AND (:maxPrice IS NULL OR r.pricePerNight <= :maxPrice)
            AND (:amenityCount = 0L OR (
                SELECT COUNT(DISTINCT a) FROM r.amenities a WHERE a IN :amenities
            ) = :amenityCount)
            AND NOT EXISTS (
                SELECT b.id FROM BookingEntity b
                WHERE b.room.id = r.id
                AND b.status <> :cancelledStatus
                AND b.deletedAt IS NULL
                AND b.checkInDate < :checkOutDate
                AND b.checkOutDate > :checkInDate
            )
            ORDER BY r.pricePerNight
            """)
    Slice<RoomEntity> search(
            @Param("cityId") Long cityId,
            @Param("hotelId") Long hotelId,
            @Param("guests") Integer guests,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("amenities") Set<Amenity> amenities,
            @Param("amenityCount") long amenityCount,
            @Param("cancelledStatus") BookingStatus cancelledStatus,
            @Param("checkInDate") LocalDateTime checkInDate,
            @Param("checkOutDate") LocalDateTime checkOutDate,
            Pageable pageable
    );
}
