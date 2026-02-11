package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uz.stajirovka.jbooking.dto.response.BookingResponse;
import uz.stajirovka.jbooking.dto.response.GuestResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.GuestEntity;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "room.hotel.name", target = "hotelName")
    @Mapping(source = "room.roomNumber", target = "roomNumber")
    @Mapping(source = "guest", target = "mainGuest", qualifiedByName = "toGuestResponse")
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
                ? entity.getAdditionalGuests().size()
                : 0;
        return 1 + additional;  // основной гость + дополнительные
    }
}
