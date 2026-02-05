package uz.stajirovka.jbooking.dto.response;

import uz.stajirovka.jbooking.constant.enums.AccommodationType;

public record HotelResponse(
        Long id,
        Long cityId,
        String cityName,
        String name,
        String description,
        Double stars,
        AccommodationType accommodationType,
        String brand
) {
}
