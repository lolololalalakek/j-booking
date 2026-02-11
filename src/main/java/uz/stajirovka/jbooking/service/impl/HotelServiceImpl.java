package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.HotelCreateRequest;
import uz.stajirovka.jbooking.dto.response.HotelResponse;
import uz.stajirovka.jbooking.entity.CityEntity;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.exception.ConflictException;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.mapper.HotelMapper;
import uz.stajirovka.jbooking.repository.CityRepository;
import uz.stajirovka.jbooking.repository.HotelRepository;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.service.HotelService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;
    private final RoomRepository roomRepository;
    private final HotelMapper hotelMapper;

    // создание нового отеля
    @Override
    @Transactional
    public HotelResponse create(HotelCreateRequest request) {
        CityEntity city = findCityById(request.cityId());

        HotelEntity entity = hotelMapper.toEntity(request);
        entity.setCity(city);
        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return hotelMapper.toResponse(hotelRepository.save(entity));
    }

    // получение отеля по идентификатору
    @Override
    public HotelResponse getById(Long id) {
        return hotelMapper.toResponse(findById(id));
    }

    // получение всех отелей с пагинацией
    @Override
    public Slice<HotelResponse> getAll(Pageable pageable) {
        return hotelRepository.findAllBy(pageable)
                .map(hotelMapper::toResponse);
    }

    // получение отелей по идентификатору города
    @Override
    public Slice<HotelResponse> getByCityId(Long cityId, Pageable pageable) {
        return hotelRepository.findByCityId(cityId, pageable)
                .map(hotelMapper::toResponse);
    }

    // обновление данных отеля
    @Override
    @Transactional
    public HotelResponse update(Long id, HotelCreateRequest request) {
        HotelEntity entity = findById(id);
        CityEntity city = findCityById(request.cityId());

        entity.setCity(city);
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setStars(request.stars());
        entity.setAccommodationType(request.accommodationType());
        entity.setUpdatedAt(LocalDateTime.now());
        return hotelMapper.toResponse(entity);
    }

    // мягкое удаление отеля по идентификатору
    @Override
    @Transactional
    public void delete(Long id) {
        HotelEntity entity = findById(id);

        // проверяем наличие активных номеров
        if (roomRepository.existsByHotelId(id)) {
            throw new ConflictException(Error.HOTEL_HAS_ROOMS);
        }

        entity.setDeletedAt(LocalDateTime.now());
    }

    // поиск отеля по идентификатору или выброс исключения
    private HotelEntity findById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.HOTEL_NOT_FOUND, "id=" + id));
    }

    // поиск города по идентификатору или выброс исключения
    private CityEntity findCityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND, "id=" + id));
    }
}
