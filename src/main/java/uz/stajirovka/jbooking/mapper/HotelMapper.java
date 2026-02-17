package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.stajirovka.jbooking.dto.response.HotelResponse;
import uz.stajirovka.jbooking.entity.CityEntity;
import uz.stajirovka.jbooking.entity.HotelEntity;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(source = "entity.id", target = "id")
    @Mapping(source = "entity.name", target = "name")
    @Mapping(source = "entity.description", target = "description")
    @Mapping(source = "entity.stars", target = "stars")
    @Mapping(source = "entity.accommodationType", target = "accommodationType")
    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.name", target = "cityName")
    HotelResponse toResponse(HotelEntity entity, CityEntity city);
}
