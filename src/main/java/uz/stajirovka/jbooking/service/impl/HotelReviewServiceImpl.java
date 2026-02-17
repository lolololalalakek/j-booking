package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.HotelReviewRequest;
import uz.stajirovka.jbooking.dto.response.HotelRatingResponse;
import uz.stajirovka.jbooking.dto.response.HotelReviewResponse;
import uz.stajirovka.jbooking.entity.GuestEntity;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.entity.HotelReviewEntity;
import uz.stajirovka.jbooking.exception.ConflictException;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.mapper.HotelReviewMapper;
import uz.stajirovka.jbooking.repository.GuestRepository;
import uz.stajirovka.jbooking.repository.HotelRepository;
import uz.stajirovka.jbooking.repository.HotelReviewRepository;
import uz.stajirovka.jbooking.service.HotelReviewService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelReviewServiceImpl implements HotelReviewService {

    private final HotelReviewRepository reviewRepository;
    private final HotelRepository hotelRepository;
    private final GuestRepository guestRepository;
    private final HotelReviewMapper hotelReviewMapper;

    @Override
    @Transactional
    public HotelReviewResponse create(Long hotelId, HotelReviewRequest request) {
        // валидируем что hotelId из path совпадает с hotelId из body
        if (!hotelId.equals(request.hotelId())) {
            throw new ConflictException(Error.REVIEW_HOTEL_MISMATCH,
                    "hotelId в URL (" + hotelId + ") не совпадает с hotelId в теле запроса (" + request.hotelId() + ")");
        }

        // проверяем что отзыв от этого гостя ещё не существует
        if (reviewRepository.existsByHotelIdAndGuestId(request.hotelId(), request.guestId())) {
            throw new ConflictException(Error.REVIEW_ALREADY_EXISTS);
        }

        HotelEntity hotel = findHotelById(request.hotelId());
        GuestEntity guest = findGuestById(request.guestId());

        HotelReviewEntity entity = new HotelReviewEntity();
        entity.setHotel(hotel);
        entity.setGuest(guest);
        entity.setRating(request.rating());
        entity.setDescription(request.description());
        entity.setCreatedAt(LocalDateTime.now());

        return hotelReviewMapper.toResponse(reviewRepository.save(entity));
    }

    @Override
    public Slice<HotelReviewResponse> getByHotelId(Long hotelId, Pageable pageable) {
        return reviewRepository.findByHotelId(hotelId, pageable)
                .map(hotelReviewMapper::toResponse);
    }



    @Override
    @Transactional
    public void delete(Long hotelId, Long reviewId) {
        HotelReviewEntity entity = findById(reviewId);
        if (!entity.getHotel().getId().equals(hotelId)) {
            throw new ConflictException(Error.REVIEW_HOTEL_MISMATCH,
                    "Отзыв id=" + reviewId + " не принадлежит отелю id=" + hotelId);
        }
        reviewRepository.delete(entity);
    }

    @Override
    public HotelRatingResponse getHotelRating(Long hotelId) {
        findHotelById(hotelId);

        Double avgRating = reviewRepository.getAverageRating(hotelId);
        long count = reviewRepository.countByHotelId(hotelId);

        return new HotelRatingResponse(hotelId, avgRating, count);
    }

    // поиск отзыва по id
    private HotelReviewEntity findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.REVIEW_NOT_FOUND, "id=" + id));
    }

    // поиск отеля по id
    private HotelEntity findHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.HOTEL_NOT_FOUND, "id=" + id));
    }

    // поиск гостя по id
    private GuestEntity findGuestById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.GUEST_NOT_FOUND, "id=" + id));
    }
}
