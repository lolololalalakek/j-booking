package uz.stajirovka.jbooking.dto.response;

import uz.stajirovka.jbooking.constant.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long roomId,
        String hotelName,
        String roomNumber,
        String guestFirstName,
        String guestLastName,
        String guestEmail,
        Integer numberOfGuests,
        LocalDateTime checkInDate,
        LocalDateTime checkOutDate,
        BookingStatus status,
        BigDecimal totalPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
