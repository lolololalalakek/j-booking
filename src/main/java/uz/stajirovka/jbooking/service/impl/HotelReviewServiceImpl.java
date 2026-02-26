package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.HotelReviewRequest;
import uz.stajirovka.jbooking.dto.response.HotelReviewResponse;
import uz.stajirovka.jbooking.dto.response.HotelReviewsResponse;
import uz.stajirovka.jbooking.entity.GuestEntity;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.entity.HotelReviewEntity;
import uz.stajirovka.jbooking.exception.ConflictException;
import uz.stajirovka.jbooking.exception.ForbiddenException;
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
        // проверяем что отзыв от этого гостя ещё не существует
        if (reviewRepository.existsByHotelIdAndGuestId(hotelId, request.guestId())) {
            throw new ConflictException(Error.REVIEW_ALREADY_EXISTS);
        }

        HotelEntity hotel = findHotelById(hotelId);
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
    public HotelReviewsResponse getByHotelId(Long hotelId, Pageable pageable) {
        findHotelById(hotelId);

        var reviews = reviewRepository.findByHotelId(hotelId, pageable)
            .map(hotelReviewMapper::toResponse);
        Double avgRating = reviewRepository.getAverageRating(hotelId);
        long count = reviewRepository.countByHotelId(hotelId);

        return new HotelReviewsResponse(avgRating, count, reviews);
    }

    @Override
    @Transactional
    public void delete(Long hotelId, Long reviewId, Long mainGuestId) {
        HotelReviewEntity entity = reviewRepository
            .findByIdAndHotelIdAndGuestId(reviewId, hotelId, mainGuestId)
            .orElseThrow(() -> new ForbiddenException(Error.REVIEW_ACCESS_DENIED));
        reviewRepository.delete(entity);
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
