package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.dto.request.CityCreateRequest;
import uz.stajirovka.jbooking.dto.response.CityResponse;
import uz.stajirovka.jbooking.entity.CityEntity;
import uz.stajirovka.jbooking.exception.ResourceNotFoundException;
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
    @Transactional
    public CityResponse create(CityCreateRequest request) {
        CityEntity entity = cityMapper.toEntity(request);
        return cityMapper.toResponse(cityRepository.save(entity));
    }

    @Override
    public CityResponse getById(Long id) {
        return cityMapper.toResponse(findById(id));
    }

    @Override
    public Slice<CityResponse> getAll(Pageable pageable) {
        return cityRepository.findAllBy(pageable)
                .map(cityMapper::toResponse);
    }

    @Override
    @Transactional
    public CityResponse update(Long id, CityCreateRequest request) {
        CityEntity entity = findById(id);
        entity.setName(request.name());
        entity.setCountry(request.country());
        return cityMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findById(id);
        cityRepository.deleteById(id);
    }

    private CityEntity findById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City", id));
    }
}
