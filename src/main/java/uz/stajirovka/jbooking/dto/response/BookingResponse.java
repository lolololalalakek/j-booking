package uz.stajirovka.jbooking.dto.response;

import uz.stajirovka.jbooking.constant.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public record BookingResponse(
        Long id,
        Long cityId,
        String cityName,
        Long hotelId,
        String hotelName,
        Long roomId,
        String roomNumber,
        GuestResponse mainGuest,
        List<GuestResponse> additionalGuests,
        Integer totalGuests,
        LocalDateTime checkInDate,
        LocalDateTime checkOutDate,
        BookingStatus status,
        Long pricePerNight,
        Long totalPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
