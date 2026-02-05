package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.stajirovka.jbooking.dto.request.BookingCreateRequest;
import uz.stajirovka.jbooking.dto.response.BookingResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BookingEntity toEntity(BookingCreateRequest request);

    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "room.hotel.name", target = "hotelName")
    @Mapping(source = "room.roomNumber", target = "roomNumber")
    BookingResponse toResponse(BookingEntity entity);
}
