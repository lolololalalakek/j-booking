package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.dto.response.RoomResponse;

public interface RoomService {

    Slice<RoomResponse> getByHotelId(long cityId, long hotelId, Currency currency, Pageable pageable);

}
