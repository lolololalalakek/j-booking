package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.dto.request.HotelCreateRequest;
import uz.stajirovka.jbooking.dto.response.HotelResponse;

public interface HotelService {

    HotelResponse create(HotelCreateRequest request);

    HotelResponse getById(Long id);

    Slice<HotelResponse> getAll(Pageable pageable);

    Slice<HotelResponse> getByCityId(Long cityId, Pageable pageable);

    HotelResponse update(Long id, HotelCreateRequest request);

    void delete(Long id);
}
