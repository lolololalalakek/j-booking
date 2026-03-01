package uz.stajirovka.jbooking.dto.response;

import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.constant.enums.AccommodationType;

public record HotelResponse(
    Long id,
    Long cityId,
    String cityName,
    String name,
    String description,
    Integer stars,
    AccommodationType accommodationType,
    Slice<RoomResponse> rooms
) {
}
