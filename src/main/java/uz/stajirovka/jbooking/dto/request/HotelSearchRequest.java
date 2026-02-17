package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import uz.stajirovka.jbooking.constant.enums.Amenity;

import java.time.LocalDateTime;
import java.util.Set;

public record HotelSearchRequest(
        // обязательные параметры
        @NotNull(message = "Дата заезда обязательна")
        @Future(message = "Дата заезда должна быть в будущем")
        LocalDateTime checkInDate,

        @NotNull(message = "Дата выезда обязательна")
        @Future(message = "Дата выезда должна быть в будущем")
        LocalDateTime checkOutDate,

        @NotNull(message = "Количество гостей обязательно")
        @Min(value = 1, message = "Минимум 1 гость")
        Integer guests,

        // опциональные фильтры
        Long cityId,
        Long minPrice,
        Long maxPrice,
        Integer minStars,
        Set<Amenity> amenities
) {}
