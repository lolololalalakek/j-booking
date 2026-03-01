package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.NotNull;

public record BookingConfirmRequest(
    @NotNull(message = "bookingId is required")
    Long bookingId
) {
}
