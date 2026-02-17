package uz.stajirovka.jbooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.stajirovka.jbooking.dto.request.HotelSearchRequest;
import uz.stajirovka.jbooking.dto.response.HotelResponse;
import uz.stajirovka.jbooking.service.HotelSearchService;
import uz.stajirovka.jbooking.service.HotelService;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final HotelSearchService hotelSearchService;

    @GetMapping
    public ResponseEntity<Slice<HotelResponse>> getByCityId(@RequestParam Long cityId,
                                                             Pageable pageable) {
        return ResponseEntity.ok(hotelService.getByCityId(cityId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<Slice<HotelResponse>> search(
            @Valid @RequestBody HotelSearchRequest request, Pageable pageable) {
        return ResponseEntity.ok(hotelSearchService.search(request, pageable));
    }
}
