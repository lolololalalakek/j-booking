package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.stajirovka.jbooking.dto.request.HotelReviewRequest;
import uz.stajirovka.jbooking.dto.response.HotelReviewResponse;
import uz.stajirovka.jbooking.entity.GuestEntity;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.entity.HotelReviewEntity;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface HotelReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "hotel", target = "hotel")
    @Mapping(source = "guest", target = "guest")
    @Mapping(source = "request.rating", target = "rating")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "createdAt", target = "createdAt")
    HotelReviewEntity toEntity(HotelReviewRequest request, HotelEntity hotel, GuestEntity guest, LocalDateTime createdAt);

    @Mapping(source = "hotel.id", target = "hotelId")
    @Mapping(source = "hotel.name", target = "hotelName")
    @Mapping(source = "guest.id", target = "guestId")
    @Mapping(target = "guestName", expression = "java(entity.getGuest().getFirstName() + \" \" + entity.getGuest().getLastName())")
    HotelReviewResponse toResponse(HotelReviewEntity entity);
}
