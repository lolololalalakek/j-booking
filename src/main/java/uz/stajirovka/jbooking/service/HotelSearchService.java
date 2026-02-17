package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.dto.request.HotelSearchRequest;
import uz.stajirovka.jbooking.dto.response.HotelResponse;

public interface HotelSearchService {

    Slice<HotelResponse> search(HotelSearchRequest request, Pageable pageable);
}
