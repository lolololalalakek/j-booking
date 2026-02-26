package uz.stajirovka.jbooking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.stajirovka.jbooking.entity.HotelReviewEntity;

import java.util.Optional;


public interface HotelReviewRepository extends JpaRepository<HotelReviewEntity, Long> {

    // отзывы по отелю
    Slice<HotelReviewEntity> findByHotelId(Long hotelId, Pageable pageable);

    // проверка существования отзыва от гостя
    boolean existsByHotelIdAndGuestId(Long hotelId, Long guestId);

    // средний рейтинг отеля
    @Query("SELECT AVG(r.rating) FROM HotelReviewEntity r WHERE r.hotel.id = :hotelId")
    Double getAverageRating(@Param("hotelId") Long hotelId);

    // поиск отзыва по id, отелю и гостю (для проверки доступа)
    Optional<HotelReviewEntity> findByIdAndHotelIdAndGuestId(Long id, Long hotelId, Long guestId);

    // количество отзывов отеля
    long countByHotelId(Long hotelId);
}
