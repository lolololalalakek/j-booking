package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.dto.response.CityResponse;

public interface CityService {

    CityResponse getById(Long id);

    Slice<CityResponse> getAll(Pageable pageable);
}
