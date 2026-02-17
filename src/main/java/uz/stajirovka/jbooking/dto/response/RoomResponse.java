package uz.stajirovka.jbooking.dto.response;

import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.constant.enums.MealPlan;
import uz.stajirovka.jbooking.constant.enums.RoomType;

import java.util.Set;

public record RoomResponse(
        Long id,
        Long hotelId,
        String hotelName,
        String roomNumber,
        RoomType roomType,
        MealPlan mealPlan,
        Set<Amenity> amenities,
        Integer capacity,
        Long pricePerNight,
        String description
) {
}
