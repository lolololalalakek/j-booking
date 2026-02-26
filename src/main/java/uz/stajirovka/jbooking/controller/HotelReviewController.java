package uz.stajirovka.jbooking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.stajirovka.jbooking.dto.request.HotelReviewRequest;
import uz.stajirovka.jbooking.dto.response.HotelReviewResponse;
import uz.stajirovka.jbooking.dto.response.HotelReviewsResponse;
import uz.stajirovka.jbooking.service.HotelReviewService;

@RestController
@RequestMapping("/api/v1/hotels/{hotelId}/reviews")
@RequiredArgsConstructor
@Validated
public class HotelReviewController {

    private final HotelReviewService reviewService;

    @PostMapping
    public ResponseEntity<HotelReviewResponse> create(@PathVariable @Positive Long hotelId,
                                                      @Valid @RequestBody HotelReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(hotelId, request));
    }

    @GetMapping
    public ResponseEntity<HotelReviewsResponse> getByHotelId(@PathVariable @Positive Long hotelId,
                                                              Pageable pageable) {
        return ResponseEntity.ok(reviewService.getByHotelId(hotelId, pageable));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long hotelId,
                                       @PathVariable @Positive Long reviewId,
                                       @RequestParam @Positive Long mainGuestId) {
        reviewService.delete(hotelId, reviewId, mainGuestId);
        return ResponseEntity.noContent().build();
    }
}
