package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.dto.response.RoomResponse;

public interface RoomService {

    RoomResponse getById(Long id, Currency currency);

    Slice<RoomResponse> getByHotelId(Long hotelId, Currency currency, Pageable pageable);

    RoomResponse updatePrice(Long id, Long newPrice);
}
