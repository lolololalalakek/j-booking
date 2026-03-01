package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RoomUpdatePriceRequest(
    @NotNull Long roomId,
    @NotNull @Positive Long newPricePerNight
) {
}
