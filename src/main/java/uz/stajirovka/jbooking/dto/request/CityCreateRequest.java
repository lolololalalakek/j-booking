package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CityCreateRequest(

    @NotBlank(message = "Name of city is required")
    String name,

    @NotBlank
    String country
) {
}
