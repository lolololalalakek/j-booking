package uz.stajirovka.jbooking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.stajirovka.jbooking.dto.response.CityResponse;
import uz.stajirovka.jbooking.service.CityService;

@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Slice<CityResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(cityService.getAll(pageable));
    }
}
