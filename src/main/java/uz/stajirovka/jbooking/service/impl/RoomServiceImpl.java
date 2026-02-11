package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.RoomCreateRequest;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.entity.RoomPriceHistoryEntity;
import uz.stajirovka.jbooking.exception.ConflictException;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.mapper.RoomMapper;
import uz.stajirovka.jbooking.repository.BookingRepository;
import uz.stajirovka.jbooking.repository.HotelRepository;
import uz.stajirovka.jbooking.repository.RoomPriceHistoryRepository;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.service.RoomService;

import java.math.BigDecimal;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final RoomPriceHistoryRepository priceHistoryRepository;
    private final RoomMapper roomMapper;

    // создание новой комнаты
    @Override
    @Transactional
    public RoomResponse create(RoomCreateRequest request) {
        HotelEntity hotel = findHotelById(request.hotelId());

        RoomEntity entity = roomMapper.toEntity(request);
        entity.setHotel(hotel);
        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        RoomEntity savedRoom = roomRepository.save(entity);

        // создаём первую запись в истории цен
        createPriceHistoryEntry(savedRoom, savedRoom.getPricePerNight(), now);

        return roomMapper.toResponse(savedRoom);
    }

    // получение комнаты по идентификатору
    @Override
    public RoomResponse getById(Long id) {
        return roomMapper.toResponse(findById(id));
    }

    // получение всех комнат с пагинацией
    @Override
    public Slice<RoomResponse> getAll(Pageable pageable) {
        return roomRepository.findAllBy(pageable)
                .map(roomMapper::toResponse);
    }

    // получение комнат по идентификатору отеля
    @Override
    public Slice<RoomResponse> getByHotelId(Long hotelId, Pageable pageable) {
        return roomRepository.findByHotelId(hotelId, pageable)
                .map(roomMapper::toResponse);
    }

    // обновление данных комнаты
    @Override
    @Transactional
    public RoomResponse update(Long id, RoomCreateRequest request) {
        RoomEntity entity = findById(id);
        HotelEntity hotel = findHotelById(request.hotelId());

        // проверяем изменение цены до обновления
        BigDecimal oldPrice = entity.getPricePerNight();
        BigDecimal newPrice = request.pricePerNight();
        boolean priceChanged = !oldPrice.equals(newPrice);

        entity.setHotel(hotel);
        entity.setRoomNumber(request.roomNumber());
        entity.setRoomType(request.roomType());
        entity.setMealPlan(request.mealPlan());
        entity.setAmenities(request.amenities());
        entity.setCapacity(request.capacity());
        entity.setPricePerNight(newPrice);
        entity.setDescription(request.description());
        LocalDateTime now = LocalDateTime.now();
        entity.setUpdatedAt(now);

        // если цена изменилась — обновляем историю
        if (priceChanged) {
            priceHistoryRepository.closeCurrentPrice(id, now);
            createPriceHistoryEntry(entity, newPrice, now);
        }

        return roomMapper.toResponse(entity);
    }

    // мягкое удаление комнаты по идентификатору
    @Override
    @Transactional
    public void delete(Long id) {
        RoomEntity entity = findById(id);

        // проверяем наличие активных бронирований (текущих или будущих)
        if (bookingRepository.existsActiveByRoomId(id, BookingStatus.CANCELLED, LocalDateTime.now())) {
            throw new ConflictException(Error.ROOM_HAS_BOOKINGS);
        }

        entity.setDeletedAt(LocalDateTime.now());
    }

    // поиск комнаты по идентификатору или выброс исключения
    private RoomEntity findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.ROOM_NOT_FOUND, "id=" + id));
    }

    // поиск отеля по идентификатору или выброс исключения
    private HotelEntity findHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.HOTEL_NOT_FOUND, "id=" + id));
    }

    // создание записи в истории цен
    private void createPriceHistoryEntry(RoomEntity room, BigDecimal price, LocalDateTime validFrom) {
        RoomPriceHistoryEntity history = new RoomPriceHistoryEntity();
        history.setRoom(room);
        history.setPricePerNight(price);
        history.setValidFrom(validFrom);
        history.setCreatedAt(LocalDateTime.now());
        priceHistoryRepository.save(history);
    }
}
