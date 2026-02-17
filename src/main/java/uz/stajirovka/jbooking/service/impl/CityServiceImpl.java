package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.response.CityResponse;
import uz.stajirovka.jbooking.entity.CityEntity;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.mapper.CityMapper;
import uz.stajirovka.jbooking.repository.CityRepository;
import uz.stajirovka.jbooking.service.CityService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public CityResponse getById(Long id) {
        return cityMapper.toResponse(findById(id));
    }

    @Override
    public Slice<CityResponse> getAll(Pageable pageable) {
        return cityRepository.findAllBy(pageable)
                .map(cityMapper::toResponse);
    }

    private CityEntity findById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND, "id=" + id));
    }
}
