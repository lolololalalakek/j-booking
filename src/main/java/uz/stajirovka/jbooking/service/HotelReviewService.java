package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.dto.request.HotelReviewRequest;
import uz.stajirovka.jbooking.dto.response.HotelRatingResponse;
import uz.stajirovka.jbooking.dto.response.HotelReviewResponse;

// сервис отзывов об отелях
public interface HotelReviewService {

    // создать отзыв
    HotelReviewResponse create(HotelReviewRequest request);

    // получить отзыв по id
    HotelReviewResponse getById(Long id);

    // получить отзывы отеля
    Slice<HotelReviewResponse> getByHotelId(Long hotelId, Pageable pageable);

    // обновить отзыв
    HotelReviewResponse update(Long id, HotelReviewRequest request);

    // удалить отзыв
    void delete(Long id);

    // получить рейтинг отеля
    HotelRatingResponse getHotelRating(Long hotelId);
}
