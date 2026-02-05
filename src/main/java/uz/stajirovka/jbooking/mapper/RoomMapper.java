package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.stajirovka.jbooking.dto.request.RoomCreateRequest;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.entity.RoomEntity;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    RoomEntity toEntity(RoomCreateRequest request);

    @Mapping(source = "hotel.id", target = "hotelId")
    @Mapping(source = "hotel.name", target = "hotelName")
    RoomResponse toResponse(RoomEntity entity);
}
