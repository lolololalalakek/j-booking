package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record HotelReviewRequest(
        @NotNull(message = "ID отеля обязателен")
        Long hotelId,

        @NotNull(message = "ID гостя обязателен")
        Long guestId,

        @NotNull(message = "Рейтинг обязателен")
        @Min(value = 1, message = "Минимальный рейтинг 1")
        @Max(value = 5, message = "Максимальный рейтинг 5")
        Integer rating,

        String description
) {}
