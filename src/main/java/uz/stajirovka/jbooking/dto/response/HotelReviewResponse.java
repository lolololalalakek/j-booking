package uz.stajirovka.jbooking.dto.response;

import java.time.LocalDateTime;

public record HotelReviewResponse(
        Long id,
        Long hotelId,
        String hotelName,
        Long guestId,
        String guestName,
        Integer rating,
        String description,
        LocalDateTime createdAt
) {}
