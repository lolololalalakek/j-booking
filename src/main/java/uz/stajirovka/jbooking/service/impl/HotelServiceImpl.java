package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.response.HotelResponse;
import uz.stajirovka.jbooking.dto.response.HotelSimpleResponse;
import uz.stajirovka.jbooking.entity.CityEntity;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.mapper.HotelMapper;
import uz.stajirovka.jbooking.repository.CityRepository;
import uz.stajirovka.jbooking.repository.HotelRepository;
import uz.stajirovka.jbooking.repository.specification.HotelSpecifications;
import uz.stajirovka.jbooking.service.HotelService;
import uz.stajirovka.jbooking.utility.BookingDateValidator;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;
    private final HotelMapper hotelMapper;


    @Override
    @Transactional(readOnly = true)
    public Slice<HotelSimpleResponse> getByCityId(Long cityId, Pageable pageable) {
        CityEntity city = cityRepository.findById(cityId)
            .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND, "id=" + cityId));
        return hotelRepository.findByCityId(cityId, pageable)
            .map(hotel -> hotelMapper.toSimpleResponse(hotel, city));
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<HotelResponse> search(LocalDateTime checkInDate,
                                       LocalDateTime checkOutDate,
                                       Integer guests,
                                       Long cityId,
                                       Long minPrice,
                                       Long maxPrice,
                                       Integer minStars,
                                       Set<Amenity> amenities,
                                       Pageable pageable) {
        BookingDateValidator.validateDates(checkInDate, checkOutDate);

        Specification<HotelEntity> specification = HotelSpecifications.search(
            checkInDate,
            checkOutDate,
            guests,
            cityId,
            minPrice,
            maxPrice,
            minStars,
            amenities
        );

        return hotelRepository.findAll(specification, pageable)
            .map(hotel -> hotelMapper.toResponse(hotel, hotel.getCity()));
    }
}
