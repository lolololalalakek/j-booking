package uz.stajirovka.jbooking.dto.response;

import org.springframework.data.domain.Slice;

public record HotelReviewsResponse(
    Double averageRating,
    Long reviewCount,
    Slice<HotelReviewResponse> reviews
) {}
