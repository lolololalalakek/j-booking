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
import uz.stajirovka.jbooking.dto.request.HotelReviewRequest;
import uz.stajirovka.jbooking.dto.response.HotelRatingResponse;
import uz.stajirovka.jbooking.dto.response.HotelReviewResponse;
import uz.stajirovka.jbooking.service.HotelReviewService;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class HotelReviewController {

    private final HotelReviewService reviewService;

    @PostMapping
    public ResponseEntity<HotelReviewResponse> create(@Valid @RequestBody HotelReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelReviewResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getById(id));
    }

    @GetMapping("/by-hotel/{hotelId}")
    public ResponseEntity<Slice<HotelReviewResponse>> getByHotelId(
            @PathVariable Long hotelId, Pageable pageable) {
        return ResponseEntity.ok(reviewService.getByHotelId(hotelId, pageable));
    }

    @GetMapping("/hotel/{hotelId}/rating")
    public ResponseEntity<HotelRatingResponse> getHotelRating(@PathVariable Long hotelId) {
        return ResponseEntity.ok(reviewService.getHotelRating(hotelId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelReviewResponse> update(
            @PathVariable Long id, @Valid @RequestBody HotelReviewRequest request) {
        return ResponseEntity.ok(reviewService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
