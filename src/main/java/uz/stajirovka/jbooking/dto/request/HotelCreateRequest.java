package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import uz.stajirovka.jbooking.constant.enums.AccommodationType;

public record HotelCreateRequest(
        @NotNull Long cityId,
        @NotBlank String name,
        String description,
        Double stars,
        @NotNull AccommodationType accommodationType,
        String brand
) {
}
