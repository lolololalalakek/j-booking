package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.dto.request.RoomSearchRequest;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.mapper.RoomMapper;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.service.CurrencyConverterService;
import uz.stajirovka.jbooking.service.RoomSearchService;
import uz.stajirovka.jbooking.utility.BookingDateValidator;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomSearchServiceImpl implements RoomSearchService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final CurrencyConverterService currencyConverter;

    @Override
    public Slice<RoomResponse> search(RoomSearchRequest request, Currency currency, Pageable pageable) {
        BookingDateValidator.validateDates(request.checkInDate(), request.checkOutDate());

        Set<Amenity> amenities = request.amenities();
        if (amenities == null || amenities.isEmpty()) {
            amenities = Set.of(Amenity.WIFI);
        }

        return roomRepository.search(
            request.cityId(),
            request.hotelId(),
            request.guests(),
            request.minPrice(),
            request.maxPrice(),
            amenities,
            amenities.size(),
            BookingStatus.CANCELLED,
            request.checkInDate(),
            request.checkOutDate(),
            pageable
        ).map(room -> roomMapper.toResponse(room, room.getHotel(), currency, currencyConverter));
    }
}
