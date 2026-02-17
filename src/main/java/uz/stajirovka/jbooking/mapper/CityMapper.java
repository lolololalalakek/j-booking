package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Mapper;
import uz.stajirovka.jbooking.dto.response.CityResponse;
import uz.stajirovka.jbooking.entity.CityEntity;

@Mapper(componentModel = "spring")
public interface CityMapper {

    CityResponse toResponse(CityEntity entity);
}
