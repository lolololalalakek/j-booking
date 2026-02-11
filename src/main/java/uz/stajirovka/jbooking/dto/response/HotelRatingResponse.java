package uz.stajirovka.jbooking.dto.response;

public record HotelRatingResponse(
        Long hotelId,
        Double averageRating,
        Long reviewCount
) {}
