package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.stajirovka.jbooking.dto.request.GuestCreateRequest;
import uz.stajirovka.jbooking.dto.response.GuestResponse;
import uz.stajirovka.jbooking.entity.GuestEntity;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)  // устанавливается в сервисе после нормализации
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    GuestEntity toEntity(GuestCreateRequest request);

    GuestResponse toResponse(GuestEntity entity);
}
