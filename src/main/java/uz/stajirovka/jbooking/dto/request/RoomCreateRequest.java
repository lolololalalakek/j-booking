package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.constant.enums.MealPlan;
import uz.stajirovka.jbooking.constant.enums.RoomType;

import java.math.BigDecimal;
import java.util.Set;

public record RoomCreateRequest(
        @NotNull Long hotelId,
        @NotBlank String roomNumber,
        @NotNull RoomType roomType,
        @NotNull MealPlan mealPlan,
        Set<Amenity> amenities,
        @NotNull @Positive Integer capacity,
        @NotNull @Positive BigDecimal pricePerNight,
        String description
) {
}
