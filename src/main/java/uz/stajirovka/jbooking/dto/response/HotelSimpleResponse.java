package uz.stajirovka.jbooking.dto.response;

import uz.stajirovka.jbooking.constant.enums.AccommodationType;

public record HotelSimpleResponse(
    Long id,
    Long cityId,
    String cityName,
    String name,
    String description,
    Integer stars,
    AccommodationType accommodationType
) {
}
