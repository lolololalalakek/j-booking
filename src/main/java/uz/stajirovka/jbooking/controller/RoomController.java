package uz.stajirovka.jbooking.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.service.RoomService;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Validated
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<Slice<RoomResponse>> findRooom(@RequestParam @Positive long cityId,
                                                         @RequestParam @Positive long hotelId,
                                                         @RequestParam(defaultValue = "UZS") Currency currency,
                                                         @RequestParam(defaultValue = "0") @PositiveOrZero int pageNumber,
                                                         @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return ResponseEntity.ok(roomService.getByHotelId(cityId, hotelId, currency, PageRequest.of(pageNumber, pageSize)));
    }
}
