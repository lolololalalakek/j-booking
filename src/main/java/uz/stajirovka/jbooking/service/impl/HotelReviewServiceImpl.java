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

    @Override
    @Transactional
    public HotelReviewResponse create(HotelReviewRequest request) {
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

        return toResponse(reviewRepository.save(entity));
    }

    @Override
    public HotelReviewResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public Slice<HotelReviewResponse> getByHotelId(Long hotelId, Pageable pageable) {
        return reviewRepository.findByHotelId(hotelId, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional
    public HotelReviewResponse update(Long id, HotelReviewRequest request) {
        HotelReviewEntity entity = findById(id);

        entity.setRating(request.rating());
        entity.setDescription(request.description());

        return toResponse(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        HotelReviewEntity entity = findById(id);
        reviewRepository.delete(entity);
    }

    @Override
    public HotelRatingResponse getHotelRating(Long hotelId) {
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

    // маппинг в response
    private HotelReviewResponse toResponse(HotelReviewEntity entity) {
        return new HotelReviewResponse(
                entity.getId(),
                entity.getHotel().getId(),
                entity.getHotel().getName(),
                entity.getGuest().getId(),
                entity.getGuest().getFirstName() + " " + entity.getGuest().getLastName(),
                entity.getRating(),
                entity.getDescription(),
                entity.getCreatedAt()
        );
    }
}
