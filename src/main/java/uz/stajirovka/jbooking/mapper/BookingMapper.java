package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.dto.response.BookingResponse;
import uz.stajirovka.jbooking.dto.response.GuestResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.GuestEntity;
import uz.stajirovka.jbooking.service.CurrencyConverterService;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "hotel.id", target = "hotelId")
    @Mapping(source = "hotel.name", target = "hotelName")
    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "room.roomNumber", target = "roomNumber")
    @Mapping(source = "mainGuest", target = "mainGuest", qualifiedByName = "toGuestResponse")
    @Mapping(source = "additionalGuests", target = "additionalGuests", qualifiedByName = "guestsToList")
    @Mapping(source = "entity", target = "totalGuests", qualifiedByName = "calculateTotalGuests")
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

    @Named("calculateTotalGuests")
    default Integer calculateTotalGuests(BookingEntity entity) {
        int additional = entity.getAdditionalGuests() != null
            ? entity.getAdditionalGuests().size() : 0;
        return 1 + additional;
    }

    @Mapping(target = "pricePerNight", expression = "java(currencyConverterService.convert(source.pricePerNight(), currency))")
    @Mapping(target = "totalPrice", expression = "java(currencyConverterService.convert(source.totalPrice(), currency))")
    BookingResponse withConvertedPrices(BookingResponse source, @Context Currency currency, @Context CurrencyConverterService currencyConverterService);
}
