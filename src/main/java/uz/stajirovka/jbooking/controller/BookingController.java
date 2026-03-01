package uz.stajirovka.jbooking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.dto.request.BookingConfirmRequest;
import uz.stajirovka.jbooking.dto.request.BookingCreateRequest;
import uz.stajirovka.jbooking.dto.request.BookingPaymentRequest;
import uz.stajirovka.jbooking.dto.response.BookingConfirmResponse;
import uz.stajirovka.jbooking.dto.response.BookingResponse;
import uz.stajirovka.jbooking.service.BookingService;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody BookingCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(bookingService.initBooking(request));
    }

    @PostMapping("/confirm")
    public ResponseEntity<BookingConfirmResponse> confirmBooking(@Valid @RequestBody BookingConfirmRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookingService.confirmBooking(request));
    }

    @PostMapping("/pay")
    public ResponseEntity<BookingConfirmResponse> payConfirmedBooking(@Valid @RequestBody BookingPaymentRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookingService.payConfirmedBooking(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById(@PathVariable @Positive Long id,
                                                   @RequestParam(defaultValue = "UZS") Currency currency) {
        return ResponseEntity.ok(bookingService.getById(id, currency));
    }

    @GetMapping("/my")
    public ResponseEntity<Slice<BookingResponse>> getAllByUser(
        @RequestParam @NotBlank String pinfl,
        @RequestParam(defaultValue = "UZS") Currency currency,
        Pageable pageable) {
        return ResponseEntity.ok(bookingService.getAllByPinfl(pinfl, currency, pageable));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancel(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(bookingService.cancel(id));
    }
}
