package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.dto.request.RoomCreateRequest;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.exception.ResourceNotFoundException;
import uz.stajirovka.jbooking.mapper.RoomMapper;
import uz.stajirovka.jbooking.repository.HotelRepository;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.service.RoomService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional
    public RoomResponse create(RoomCreateRequest request) {
        HotelEntity hotel = findHotelById(request.hotelId());

        RoomEntity entity = roomMapper.toEntity(request);
        entity.setHotel(hotel);
        return roomMapper.toResponse(roomRepository.save(entity));
    }

    @Override
    public RoomResponse getById(Long id) {
        return roomMapper.toResponse(findById(id));
    }

    @Override
    public Slice<RoomResponse> getAll(Pageable pageable) {
        return roomRepository.findAllBy(pageable)
                .map(roomMapper::toResponse);
    }

    @Override
    public Slice<RoomResponse> getByHotelId(Long hotelId, Pageable pageable) {
        return roomRepository.findByHotelId(hotelId, pageable)
                .map(roomMapper::toResponse);
    }

    @Override
    @Transactional
    public RoomResponse update(Long id, RoomCreateRequest request) {
        RoomEntity entity = findById(id);
        HotelEntity hotel = findHotelById(request.hotelId());

        entity.setHotel(hotel);
        entity.setRoomNumber(request.roomNumber());
        entity.setCapacity(request.capacity());
        entity.setPricePerNight(request.pricePerNight());
        entity.setDescription(request.description());
        return roomMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findById(id);
        roomRepository.deleteById(id);
    }

    private RoomEntity findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", id));
    }

    private HotelEntity findHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", id));
    }
}
