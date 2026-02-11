package uz.stajirovka.jbooking.dto.response;

import uz.stajirovka.jbooking.constant.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BookingResponse(
        Long id,
        Long roomId,
        String hotelName,
        String roomNumber,
        GuestResponse mainGuest,
        List<GuestResponse> additionalGuests,
        Integer totalGuests,
        LocalDateTime checkInDate,
        LocalDateTime checkOutDate,
        BookingStatus status,
        BigDecimal totalPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
