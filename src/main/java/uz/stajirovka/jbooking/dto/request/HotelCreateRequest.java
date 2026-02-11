package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import uz.stajirovka.jbooking.constant.enums.AccommodationType;

public record HotelCreateRequest(
        @NotNull(message = "ID города обязателен")
        Long cityId,

        @NotBlank(message = "Название отеля обязательно")
        String name,

        String description,

        @Min(value = 1, message = "Минимум 1 звезда")
        @Max(value = 5, message = "Максимум 5 звёзд")
        Integer stars,

        @NotNull(message = "Тип размещения обязателен")
        AccommodationType accommodationType
) {
}
