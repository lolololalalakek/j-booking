package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.dto.request.HotelCreateRequest;
import uz.stajirovka.jbooking.dto.response.HotelResponse;
import uz.stajirovka.jbooking.entity.CityEntity;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.exception.ResourceNotFoundException;
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
    @Transactional
    public HotelResponse create(HotelCreateRequest request) {
        CityEntity city = findCityById(request.cityId());

        HotelEntity entity = hotelMapper.toEntity(request);
        entity.setCity(city);
        return hotelMapper.toResponse(hotelRepository.save(entity));
    }

    @Override
    public HotelResponse getById(Long id) {
        return hotelMapper.toResponse(findById(id));
    }

    @Override
    public Slice<HotelResponse> getAll(Pageable pageable) {
        return hotelRepository.findAllBy(pageable)
                .map(hotelMapper::toResponse);
    }

    @Override
    public Slice<HotelResponse> getByCityId(Long cityId, Pageable pageable) {
        return hotelRepository.findByCityId(cityId, pageable)
                .map(hotelMapper::toResponse);
    }

    @Override
    @Transactional
    public HotelResponse update(Long id, HotelCreateRequest request) {
        HotelEntity entity = findById(id);
        CityEntity city = findCityById(request.cityId());

        entity.setCity(city);
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setStars(request.stars());
        entity.setAccommodationType(request.accommodationType());
        entity.setBrand(request.brand());
        return hotelMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findById(id);
        hotelRepository.deleteById(id);
    }

    private HotelEntity findById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", id));
    }

    private CityEntity findCityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City", id));
    }
}
