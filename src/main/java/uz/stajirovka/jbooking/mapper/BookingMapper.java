package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.dto.request.BookingCreateRequest;
import uz.stajirovka.jbooking.dto.response.BookingResponse;
import uz.stajirovka.jbooking.dto.response.GuestResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.GuestEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.service.CurrencyConverterService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "room.hotel.city", target = "city")
    @Mapping(source = "room.hotel", target = "hotel")
    @Mapping(source = "room", target = "room")
    @Mapping(source = "mainGuest", target = "mainGuest")
    @Mapping(source = "additionalGuests", target = "additionalGuests")
    @Mapping(source = "request.checkInDate", target = "checkInDate")
    @Mapping(source = "request.checkOutDate", target = "checkOutDate")
    @Mapping(target = "status", constant = "HOLD")
    @Mapping(source = "room.pricePerNight", target = "pricePerNight")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "totalGuests", target = "totalGuests")
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "notificationId", ignore = true)
    @Mapping(source = "now", target = "createdAt")
    @Mapping(source = "now", target = "updatedAt")
    @Mapping(target = "deletedAt", ignore = true)
    BookingEntity toEntity(
        BookingCreateRequest request,
        RoomEntity room,
        GuestEntity mainGuest,
        Set<GuestEntity> additionalGuests,
        Long totalPrice,
        Integer totalGuests,
        LocalDateTime now
    );

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "hotel.id", target = "hotelId")
    @Mapping(source = "hotel.name", target = "hotelName")
    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "room.roomNumber", target = "roomNumber")
    @Mapping(source = "mainGuest", target = "mainGuest", qualifiedByName = "toGuestResponse")
    @Mapping(source = "additionalGuests", target = "additionalGuests", qualifiedByName = "guestsToList")
    @Mapping(source = "totalGuests", target = "totalGuests")
    BookingResponse toResponse(BookingEntity entity);

    @Named("toGuestResponse")
    default GuestResponse toGuestResponse(GuestEntity entity) {
        if (entity == null) {
            return null;
        }
        return new GuestResponse(
            entity.getId(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getPinfl(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getCreatedAt()
        );
    }

    @Named("guestsToList")
    default List<GuestResponse> guestsToList(Set<GuestEntity> guests) {
        if (guests == null) {
            return List.of();
        }
        return guests.stream()
            .map(this::toGuestResponse)
            .toList();
    }

    @Mapping(target = "pricePerNight", expression = "java(currencyConverterService.convert(source.pricePerNight(), currency))")
    @Mapping(target = "totalPrice", expression = "java(currencyConverterService.convert(source.totalPrice(), currency))")
    BookingResponse withConvertedPrices(BookingResponse source, @Context Currency currency, @Context CurrencyConverterService currencyConverterService);
}
