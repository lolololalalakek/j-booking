package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.stajirovka.jbooking.dto.request.CityCreateRequest;
import uz.stajirovka.jbooking.dto.response.CityResponse;
import uz.stajirovka.jbooking.entity.CityEntity;

@Mapper(componentModel = "spring")
public interface CityMapper {

    @Mapping(target = "id", ignore = true)
    CityEntity toEntity(CityCreateRequest request);

    CityResponse toResponse(CityEntity entity);
}
