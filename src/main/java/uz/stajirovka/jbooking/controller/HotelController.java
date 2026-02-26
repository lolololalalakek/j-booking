package uz.stajirovka.jbooking.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.stajirovka.jbooking.dto.response.HotelResponse;
import uz.stajirovka.jbooking.service.HotelService;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
@Validated
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(hotelService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Slice<HotelResponse>> getByCityId(@RequestParam @Positive Long cityId,
                                                            Pageable pageable) {
        return ResponseEntity.ok(hotelService.getByCityId(cityId, pageable));
    }
}
