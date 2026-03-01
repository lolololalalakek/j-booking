package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.RoomUpdatePriceRequest;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.entity.RoomPriceHistoryEntity;
import uz.stajirovka.jbooking.exception.ConflictException;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.mapper.RoomMapper;
import uz.stajirovka.jbooking.repository.RoomPriceHistoryRepository;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.service.AdminHotelService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminHotelServiceImpl implements AdminHotelService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final RoomPriceHistoryRepository roomPriceHistoryRepository;

    @Override
    @Transactional
    public RoomResponse updateRoomPrice(RoomUpdatePriceRequest request) {
        RoomEntity roomEntity = roomRepository.findById(request.roomId())
            .orElseThrow(() -> new NotFoundException(Error.ROOM_NOT_FOUND));
        if (roomEntity.getPricePerNight().compareTo(request.newPricePerNight()) == 0L) {
            throw new ConflictException(Error.ROOM_PRICE_EQUALS_TO_NEW);
        }
        roomPriceHistoryRepository.findByRoomId(request.roomId())
            .ifPresentOrElse(history -> {
                    LocalDateTime now = LocalDateTime.now();
                    history.setValidTo(now);
                    RoomPriceHistoryEntity roomPriceHistoryEntity = getNewRoomPriceHistoryEntity(request, roomEntity, now);
                    roomPriceHistoryRepository.saveAll(List.of(history, roomPriceHistoryEntity));
                },
                () -> {
                    RoomPriceHistoryEntity newRoomPriceHistoryEntity = getNewRoomPriceHistoryEntity(request, roomEntity, LocalDateTime.now());
                    roomPriceHistoryRepository.save(newRoomPriceHistoryEntity);
                });

        roomEntity.setPricePerNight(request.newPricePerNight());
        RoomEntity saved = roomRepository.save(roomEntity);
        return roomMapper.toResponse(saved);
    }

    private static @NonNull RoomPriceHistoryEntity getNewRoomPriceHistoryEntity(RoomUpdatePriceRequest request, RoomEntity roomEntity, LocalDateTime now) {
        RoomPriceHistoryEntity roomPriceHistoryEntity = new RoomPriceHistoryEntity();
        roomPriceHistoryEntity.setRoom(roomEntity);
        roomPriceHistoryEntity.setPricePerNight(request.newPricePerNight());
        roomPriceHistoryEntity.setValidFrom(now);
        return roomPriceHistoryEntity;
    }
}

