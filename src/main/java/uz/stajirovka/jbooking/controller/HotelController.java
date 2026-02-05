package uz.stajirovka.jbooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.stajirovka.jbooking.dto.request.HotelCreateRequest;
import uz.stajirovka.jbooking.dto.response.HotelResponse;
import uz.stajirovka.jbooking.service.HotelService;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelResponse> create(@Valid @RequestBody HotelCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Slice<HotelResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(hotelService.getAll(pageable));
    }

    @GetMapping("/by-city/{cityId}")
    public ResponseEntity<Slice<HotelResponse>> getByCityId(@PathVariable Long cityId,
                                                             Pageable pageable) {
        return ResponseEntity.ok(hotelService.getByCityId(cityId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody HotelCreateRequest request) {
        return ResponseEntity.ok(hotelService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hotelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
