package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.HotelSearchRequest;
import uz.stajirovka.jbooking.dto.response.HotelResponse;
import uz.stajirovka.jbooking.entity.CityEntity;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.mapper.HotelMapper;
import uz.stajirovka.jbooking.repository.CityRepository;
import uz.stajirovka.jbooking.repository.HotelRepository;
import uz.stajirovka.jbooking.service.HotelSearchService;
import uz.stajirovka.jbooking.utility.BookingDateValidator;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelSearchServiceImpl implements HotelSearchService {

    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;
    private final HotelMapper hotelMapper;

    @Override
    public Slice<HotelResponse> search(HotelSearchRequest request, Pageable pageable) {
        BookingDateValidator.validateDates(request.checkInDate(), request.checkOutDate());

        Set<Amenity> amenities = request.amenities();
        long amenityCount = (amenities != null && !amenities.isEmpty()) ? amenities.size() : 0L;

        // если amenities пустой, передаём Set с dummy-значением (не будет использован, т.к. amenityCount = 0)
        if (amenities == null || amenities.isEmpty()) {
            amenities = Set.of(Amenity.WIFI);
        }

        return hotelRepository.search(
                request.cityId(),
                request.minStars(),
                request.guests(),
                request.minPrice(),
                request.maxPrice(),
                amenities,
                amenityCount,
                BookingStatus.CANCELLED,
                request.checkInDate(),
                request.checkOutDate(),
                pageable
        ).map(hotel -> {
            CityEntity city = cityRepository.findByHotelId(hotel.getId())
                    .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND, "hotelId=" + hotel.getId()));
            return hotelMapper.toResponse(hotel, city);
        });
    }
}
