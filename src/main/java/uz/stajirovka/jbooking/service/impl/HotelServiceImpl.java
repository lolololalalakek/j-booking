package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.response.HotelResponse;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.entity.CityEntity;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.mapper.HotelMapper;
import uz.stajirovka.jbooking.mapper.RoomMapper;
import uz.stajirovka.jbooking.repository.CityRepository;
import uz.stajirovka.jbooking.repository.HotelRepository;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.repository.specification.HotelSpecifications;
import uz.stajirovka.jbooking.repository.specification.RoomSpecifications;
import uz.stajirovka.jbooking.service.HotelService;
import uz.stajirovka.jbooking.utility.BookingDateValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;
    private final HotelMapper hotelMapper;
    private final RoomMapper roomMapper;
    private final RoomRepository roomRepository;


    @Override
    public Slice<HotelResponse> getByCityId(Long cityId, Pageable pageable) {
        CityEntity city = cityRepository.findById(cityId)
            .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND, "id=" + cityId));
        return hotelRepository.findByCityId(cityId, pageable)
            .map(hotel -> mapHotelResponse(hotel, city));
    }

    @Override
    public Slice<HotelResponse> search(LocalDateTime checkInDate,
                                       LocalDateTime checkOutDate,
                                       Integer guests,
                                       Long cityId,
                                       Long minPrice,
                                       Long maxPrice,
                                       Integer minStars,
                                       Set<Amenity> amenities,
                                       Pageable pageable) {
        BookingDateValidator.validateDates(checkInDate, checkOutDate);

        Specification<HotelEntity> specification = HotelSpecifications.search(
            checkInDate,
            checkOutDate,
            guests,
            cityId,
            minPrice,
            maxPrice,
            minStars,
            amenities
        );

        return hotelRepository.findAll(specification, pageable)
            .map(hotel -> mapHotelResponse(
                hotel,
                hotel.getCity(),
                checkInDate,
                checkOutDate,
                guests,
                minPrice,
                maxPrice,
                amenities
            ));
    }

    private HotelResponse mapHotelResponse(HotelEntity hotel, CityEntity city) {
        List<RoomResponse> roomResponses = hotel.getRooms() == null ? List.of() : hotel.getRooms().stream()
            .filter(room -> room.getDeletedAt() == null)
            .map(roomMapper::toResponse)
            .toList();
        Slice<RoomResponse> roomsSlice = new SliceImpl<>(roomResponses);
        return hotelMapper.toResponse(hotel, city, roomsSlice);
    }

    private HotelResponse mapHotelResponse(HotelEntity hotel,
                                           CityEntity city,
                                           LocalDateTime checkInDate,
                                           LocalDateTime checkOutDate,
                                           Integer guests,
                                           Long minPrice,
                                           Long maxPrice,
                                           Set<Amenity> amenities) {
        Specification<RoomEntity> roomSpec = RoomSpecifications.availableForHotel(
            hotel.getId(), checkInDate, checkOutDate, guests, minPrice, maxPrice, amenities);
        List<RoomResponse> roomResponses = roomRepository.findAll(roomSpec).stream()
            .map(roomMapper::toResponse)
            .toList();
        return hotelMapper.toResponse(hotel, city, new SliceImpl<>(roomResponses));
    }
}

