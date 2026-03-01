package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.dto.response.HotelResponse;

import java.time.LocalDateTime;
import java.util.Set;

public interface HotelService {


    Slice<HotelResponse> getByCityId(Long cityId, Pageable pageable);

    Slice<HotelResponse> search(
        LocalDateTime checkInDate,
        LocalDateTime checkOutDate,
        Integer guests,
        Long cityId,
        Long minPrice,
        Long maxPrice,
        Integer minStars,
        Set<Amenity> amenities,
        Pageable pageable
    );
}
