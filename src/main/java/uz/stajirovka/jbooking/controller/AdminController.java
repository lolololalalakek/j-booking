package uz.stajirovka.jbooking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uz.stajirovka.jbooking.dto.request.RoomUpdatePriceRequest;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.service.AdminHotelService;

@RestController
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final AdminHotelService adminHotelService;

    @PatchMapping("/api/v1/update-room-price")
    public ResponseEntity<RoomResponse> updateRoomPrice(
        @RequestBody RoomUpdatePriceRequest request
    ) {
        return ResponseEntity.ok(adminHotelService.updateRoomPrice(request));

    }


}
