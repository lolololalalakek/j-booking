package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.stajirovka.jbooking.dto.response.HotelReviewResponse;
import uz.stajirovka.jbooking.entity.HotelReviewEntity;

@Mapper(componentModel = "spring")
public interface HotelReviewMapper {

    @Mapping(source = "hotel.id", target = "hotelId")
    @Mapping(source = "hotel.name", target = "hotelName")
    @Mapping(source = "guest.id", target = "guestId")
    @Mapping(target = "guestName", expression = "java(entity.getGuest().getFirstName() + \" \" + entity.getGuest().getLastName())")
    HotelReviewResponse toResponse(HotelReviewEntity entity);
}
