package uz.stajirovka.jbooking.service;

import uz.stajirovka.jbooking.dto.request.RoomUpdatePriceRequest;
import uz.stajirovka.jbooking.dto.response.RoomResponse;

public interface AdminHotelService {

    RoomResponse updateRoomPrice(RoomUpdatePriceRequest request);
}
