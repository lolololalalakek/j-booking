package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.service.CurrencyConverterService;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(source = "entity.id", target = "id")
    @Mapping(source = "entity.roomNumber", target = "roomNumber")
    @Mapping(source = "entity.roomType", target = "roomType")
    @Mapping(source = "entity.mealPlan", target = "mealPlan")
    @Mapping(source = "entity.amenities", target = "amenities")
    @Mapping(source = "entity.capacity", target = "capacity")
    @Mapping(source = "entity.pricePerNight", target = "pricePerNight")
    @Mapping(source = "entity.description", target = "description")
    @Mapping(source = "hotel.id", target = "hotelId")
    @Mapping(source = "hotel.name", target = "hotelName")
    RoomResponse toResponse(RoomEntity entity, HotelEntity hotel);

    @Mapping(source = "entity.id", target = "id")
    @Mapping(source = "entity.roomNumber", target = "roomNumber")
    @Mapping(source = "entity.roomType", target = "roomType")
    @Mapping(source = "entity.mealPlan", target = "mealPlan")
    @Mapping(source = "entity.amenities", target = "amenities")
    @Mapping(source = "entity.capacity", target = "capacity")
    @Mapping(source = "entity.description", target = "description")
    @Mapping(source = "hotel.id", target = "hotelId")
    @Mapping(source = "hotel.name", target = "hotelName")
    @Mapping(target = "pricePerNight", expression = "java(currencyConverterService.convert(entity.getPricePerNight(), currency))")
    RoomResponse toResponse(RoomEntity entity, HotelEntity hotel, @Context Currency currency, @Context CurrencyConverterService currencyConverterService);
}
