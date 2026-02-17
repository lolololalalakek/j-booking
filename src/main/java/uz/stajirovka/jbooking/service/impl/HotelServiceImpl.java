package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.response.HotelResponse;
import uz.stajirovka.jbooking.entity.CityEntity;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.mapper.HotelMapper;
import uz.stajirovka.jbooking.repository.CityRepository;
import uz.stajirovka.jbooking.repository.HotelRepository;
import uz.stajirovka.jbooking.service.HotelService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;
    private final HotelMapper hotelMapper;

    @Override
    public HotelResponse getById(Long id) {
        HotelEntity hotel = findById(id);
        CityEntity city = cityRepository.findByHotelId(id)
                .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND, "hotelId=" + id));
        return hotelMapper.toResponse(hotel, city);
    }

    @Override
    public Slice<HotelResponse> getByCityId(Long cityId, Pageable pageable) {
        CityEntity city = cityRepository.findById(cityId)
                .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND, "id=" + cityId));
        return hotelRepository.findByCityId(cityId, pageable)
                .map(hotel -> hotelMapper.toResponse(hotel, city));
    }

    private HotelEntity findById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.HOTEL_NOT_FOUND, "id=" + id));
    }
}
