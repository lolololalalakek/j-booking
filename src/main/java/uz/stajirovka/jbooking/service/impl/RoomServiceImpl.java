package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.entity.RoomPriceHistoryEntity;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.mapper.RoomMapper;
import uz.stajirovka.jbooking.repository.HotelRepository;
import uz.stajirovka.jbooking.repository.RoomPriceHistoryRepository;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.service.CurrencyConverterService;
import uz.stajirovka.jbooking.service.RoomService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomPriceHistoryRepository priceHistoryRepository;
    private final RoomMapper roomMapper;
    private final CurrencyConverterService currencyConverter;

    @Override
    public RoomResponse getById(Long id, Currency currency) {
        RoomEntity room = findById(id);
        HotelEntity hotel = hotelRepository.findByRoomId(id)
                .orElseThrow(() -> new NotFoundException(Error.HOTEL_NOT_FOUND, "roomId=" + id));
        RoomResponse response = roomMapper.toResponse(room, hotel);
        return convertPrice(response, currency);
    }

    @Override
    public Slice<RoomResponse> getByHotelId(Long hotelId, Currency currency, Pageable pageable) {
        HotelEntity hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NotFoundException(Error.HOTEL_NOT_FOUND, "id=" + hotelId));
        return roomRepository.findByHotelId(hotelId, pageable)
                .map(room -> roomMapper.toResponse(room, hotel))
                .map(r -> convertPrice(r, currency));
    }

    @Override
    @Transactional
    public RoomResponse updatePrice(Long id, Long newPrice) {
        RoomEntity room = findById(id);
        HotelEntity hotel = hotelRepository.findByRoomId(id)
                .orElseThrow(() -> new NotFoundException(Error.HOTEL_NOT_FOUND, "roomId=" + id));
        LocalDateTime now = LocalDateTime.now();

        priceHistoryRepository.closeCurrentPrice(room.getId(), now);

        RoomPriceHistoryEntity history = RoomPriceHistoryEntity.builder()
                .room(room)
                .pricePerNight(newPrice)
                .validFrom(now)
                .createdAt(now)
                .build();
        priceHistoryRepository.save(history);

        room.setPricePerNight(newPrice);
        room.setUpdatedAt(now);

        return roomMapper.toResponse(room, hotel);
    }

    private RoomResponse convertPrice(RoomResponse response, Currency currency) {
        return new RoomResponse(
                response.id(),
                response.hotelId(),
                response.hotelName(),
                response.roomNumber(),
                response.roomType(),
                response.mealPlan(),
                response.amenities(),
                response.capacity(),
                currencyConverter.convert(response.pricePerNight(), currency),
                response.description()
        );
    }

    private RoomEntity findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.ROOM_NOT_FOUND, "id=" + id));
    }
}
