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
import uz.stajirovka.jbooking.dto.request.CityCreateRequest;
import uz.stajirovka.jbooking.dto.response.CityResponse;
import uz.stajirovka.jbooking.service.CityService;

@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @PostMapping
    public ResponseEntity<CityResponse> create(@Valid @RequestBody CityCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cityService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Slice<CityResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(cityService.getAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody CityCreateRequest request) {
        return ResponseEntity.ok(cityService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
