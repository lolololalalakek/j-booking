package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.dto.response.HotelResponse;

public interface HotelService {

    HotelResponse getById(Long id);

    Slice<HotelResponse> getByCityId(Long cityId, Pageable pageable);
}
