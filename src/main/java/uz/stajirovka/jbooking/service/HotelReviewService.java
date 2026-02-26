package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import uz.stajirovka.jbooking.dto.request.HotelReviewRequest;
import uz.stajirovka.jbooking.dto.response.HotelReviewResponse;
import uz.stajirovka.jbooking.dto.response.HotelReviewsResponse;

// сервис отзывов об отелях
public interface HotelReviewService {

    HotelReviewResponse create(Long hotelId, HotelReviewRequest request);

    // получить отзывы отеля вместе с рейтингом
    HotelReviewsResponse getByHotelId(Long hotelId, Pageable pageable);

    // удалить отзыв
    void delete(Long hotelId, Long reviewId, Long mainGuestId);
}
