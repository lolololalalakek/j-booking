package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.dto.request.CityCreateRequest;
import uz.stajirovka.jbooking.dto.response.CityResponse;

public interface CityService {

    CityResponse create(CityCreateRequest request);

    CityResponse getById(Long id);

    Slice<CityResponse> getAll(Pageable pageable);

    CityResponse update(Long id, CityCreateRequest request);

    void delete(Long id);
}
