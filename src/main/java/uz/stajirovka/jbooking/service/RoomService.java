package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.dto.request.RoomCreateRequest;
import uz.stajirovka.jbooking.dto.response.RoomResponse;

public interface RoomService {

    RoomResponse create(RoomCreateRequest request);

    RoomResponse getById(Long id);

    Slice<RoomResponse> getAll(Pageable pageable);

    Slice<RoomResponse> getByHotelId(Long hotelId, Pageable pageable);

    RoomResponse update(Long id, RoomCreateRequest request);

    void delete(Long id);
}
