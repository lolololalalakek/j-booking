package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.stajirovka.jbooking.dto.request.GuestInfoRequest;
import uz.stajirovka.jbooking.entity.GuestEntity;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "guestInfo.firstName", target = "firstName")
    @Mapping(source = "guestInfo.lastName", target = "lastName")
    @Mapping(source = "guestInfo.pinfl", target = "pinfl")
    @Mapping(source = "guestInfo.email", target = "email")
    @Mapping(source = "guestInfo.phone", target = "phone")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(target = "deletedAt", ignore = true)
    GuestEntity toEntity(GuestInfoRequest guestInfo, LocalDateTime createdAt);
}
