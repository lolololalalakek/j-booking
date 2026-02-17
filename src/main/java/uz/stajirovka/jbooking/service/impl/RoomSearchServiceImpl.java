package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.RoomSearchRequest;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.mapper.RoomMapper;
import uz.stajirovka.jbooking.repository.HotelRepository;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.service.RoomSearchService;
import uz.stajirovka.jbooking.utility.BookingDateValidator;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomSearchServiceImpl implements RoomSearchService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;

    @Override
    public Slice<RoomResponse> search(RoomSearchRequest request, Pageable pageable) {
        BookingDateValidator.validateDates(request.checkInDate(), request.checkOutDate());

        Set<Amenity> amenities = request.amenities();
        long amenityCount = (amenities != null && !amenities.isEmpty()) ? amenities.size() : 0L;

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
                amenityCount,
                BookingStatus.CANCELLED,
                request.checkInDate(),
                request.checkOutDate(),
                pageable
        ).map(room -> {
            HotelEntity hotel = hotelRepository.findByRoomId(room.getId())
                    .orElseThrow(() -> new NotFoundException(Error.HOTEL_NOT_FOUND, "roomId=" + room.getId()));
            return roomMapper.toResponse(room, hotel);
        });
    }
}
