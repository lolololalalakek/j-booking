package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.mapper.RoomMapper;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.service.CurrencyConverterService;
import uz.stajirovka.jbooking.service.RoomService;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final CurrencyConverterService currencyConverter;


    @Override
    public Slice<RoomResponse> getByHotelId(long cityId, long hotelId, Currency currency, Pageable pageable) {

        return roomRepository.findByHotel_IdAndHotel_City_Id(hotelId, cityId, pageable)
            .map(room -> roomMapper.toResponse(room, currency, currencyConverter));
    }

}
