package uz.stajirovka.jbooking.dto.response;

import java.math.BigDecimal;

public record RoomResponse(
        Long id,
        Long hotelId,
        String hotelName,
        String roomNumber,
        Integer capacity,
        BigDecimal pricePerNight,
        String description
) {
}
