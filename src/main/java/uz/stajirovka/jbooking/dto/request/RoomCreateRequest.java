package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RoomCreateRequest(
        @NotNull Long hotelId,
        @NotBlank String roomNumber,
        @NotNull @Positive Integer capacity,
        @NotNull @Positive BigDecimal pricePerNight,
        String description
) {
}
