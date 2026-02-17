package uz.stajirovka.jbooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.dto.request.RoomSearchRequest;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.service.RoomSearchService;
import uz.stajirovka.jbooking.service.RoomService;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Validated
public class RoomController {

    private final RoomService roomService;
    private final RoomSearchService roomSearchService;

    @GetMapping
    public ResponseEntity<Slice<RoomResponse>> getByHotelId(@RequestParam Long hotelId,
                                                             @RequestParam(defaultValue = "UZS") Currency currency,
                                                             Pageable pageable) {
        return ResponseEntity.ok(roomService.getByHotelId(hotelId, currency, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getById(@PathVariable Long id,
                                                 @RequestParam(defaultValue = "UZS") Currency currency) {
        return ResponseEntity.ok(roomService.getById(id, currency));
    }

    @PostMapping("/search")
    public ResponseEntity<Slice<RoomResponse>> search(
            @Valid @RequestBody RoomSearchRequest request, Pageable pageable) {
        return ResponseEntity.ok(roomSearchService.search(request, pageable));
    }

    // обновление цены комнаты (для админов)
    @PatchMapping("/{id}/price")
    public ResponseEntity<RoomResponse> updatePrice(@PathVariable Long id,
                                                     @RequestParam @NotNull @Positive Long price) {
        return ResponseEntity.ok(roomService.updatePrice(id, price));
    }
}
