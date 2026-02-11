package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CityCreateRequest(

    @NotBlank(message = "Название города обязательно")
    String name,

    @NotBlank(message = "Страна обязательна")
    String country
) {
}
