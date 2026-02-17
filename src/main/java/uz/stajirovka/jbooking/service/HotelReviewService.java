package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.dto.request.HotelReviewRequest;
import uz.stajirovka.jbooking.dto.response.HotelRatingResponse;
import uz.stajirovka.jbooking.dto.response.HotelReviewResponse;

// сервис отзывов об отелях
public interface HotelReviewService {

    HotelReviewResponse create(Long hotelId, HotelReviewRequest request);

    // получить отзывы отеля
    Slice<HotelReviewResponse> getByHotelId(Long hotelId, Pageable pageable);

    // удалить отзыв
    void delete(Long hotelId, Long reviewId);

    // получить рейтинг отеля
    HotelRatingResponse getHotelRating(Long hotelId);
}
