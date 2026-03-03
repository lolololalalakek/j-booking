package uz.stajirovka.jbooking.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.dto.response.HotelResponse;
import uz.stajirovka.jbooking.dto.response.HotelSimpleResponse;
import uz.stajirovka.jbooking.service.HotelService;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
@Validated
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<Slice<HotelSimpleResponse>> getByCityId(@RequestParam @Positive Long cityId,
                                                                  Pageable pageable) {
        return ResponseEntity.ok(hotelService.getByCityId(cityId, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Slice<HotelResponse>> search(
        @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkInDate,
        @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkOutDate,
        @RequestParam(defaultValue = "2") @Positive Integer guests,
        @RequestParam @NotNull @Positive Long cityId,
        @RequestParam @NotNull @PositiveOrZero Long minPrice,
        @RequestParam(required = false) @PositiveOrZero Long maxPrice,
        @RequestParam(required = false) @Min(1) @Max(5) Integer minStars,
        @RequestParam(required = false) @Size(min = 1) Set<Amenity> amenities,
        Pageable pageable) {
        return ResponseEntity.ok(hotelService.search(
            checkInDate, checkOutDate, guests, cityId, minPrice, maxPrice, minStars, amenities, pageable));
    }
}
