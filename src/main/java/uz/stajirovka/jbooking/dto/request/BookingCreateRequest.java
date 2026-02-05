package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record BookingCreateRequest(
        @NotNull Long roomId,
        @NotBlank String guestFirstName,
        @NotBlank String guestLastName,
        String guestEmail,
        @NotNull @Positive Integer numberOfGuests,
        @NotNull LocalDateTime checkInDate,
        @NotNull LocalDateTime checkOutDate
) {
}
