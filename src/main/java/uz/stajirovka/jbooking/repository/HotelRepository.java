package uz.stajirovka.jbooking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.entity.HotelEntity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

    @Query("SELECT h FROM CityEntity c JOIN c.hotels h WHERE c.id = :cityId AND h.deletedAt IS NULL")
    Slice<HotelEntity> findByCityId(@Param("cityId") Long cityId, Pageable pageable);

    @Query("SELECT h FROM HotelEntity h JOIN h.rooms r WHERE r.id = :roomId")
    Optional<HotelEntity> findByRoomId(@Param("roomId") Long roomId);

    @Query("""
            SELECT DISTINCT h FROM HotelEntity h
            LEFT JOIN CityEntity c ON h MEMBER OF c.hotels
            WHERE h.deletedAt IS NULL
            AND (:cityId IS NULL OR c.id = :cityId)
            AND (:minStars IS NULL OR h.stars >= :minStars)
            AND EXISTS (
                SELECT r.id FROM HotelEntity h2 JOIN h2.rooms r
                WHERE h2.id = h.id
                AND r.deletedAt IS NULL
                AND r.capacity >= :guests
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
            )
            ORDER BY h.name
            """)
    Slice<HotelEntity> search(
            @Param("cityId") Long cityId,
            @Param("minStars") Integer minStars,
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
