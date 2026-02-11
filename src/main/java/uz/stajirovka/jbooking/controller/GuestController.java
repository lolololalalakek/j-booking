package uz.stajirovka.jbooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.stajirovka.jbooking.dto.request.GuestCreateRequest;
import uz.stajirovka.jbooking.dto.response.BookingResponse;
import uz.stajirovka.jbooking.dto.response.GuestResponse;
import uz.stajirovka.jbooking.service.GuestService;

@RestController
@RequestMapping("/api/v1/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    // создание нового гостя
    @PostMapping
    public ResponseEntity<GuestResponse> create(@Valid @RequestBody GuestCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guestService.create(request));
    }

    // получение гостя по идентификатору
    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(guestService.getById(id));
    }

    // получение всех гостей с пагинацией
    @GetMapping
    public ResponseEntity<Slice<GuestResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(guestService.getAll(pageable));
    }

    // получение гостя по email
    @GetMapping("/by-email")
    public ResponseEntity<GuestResponse> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(guestService.getByEmail(email));
    }

    // обновление данных гостя
    @PutMapping("/{id}")
    public ResponseEntity<GuestResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody GuestCreateRequest request) {
        return ResponseEntity.ok(guestService.update(id, request));
    }

    // удаление гостя
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        guestService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // получение бронирований гостя по email
    @GetMapping("/by-email/bookings")
    public ResponseEntity<Slice<BookingResponse>> getBookingsByEmail(
            @RequestParam String email, Pageable pageable) {
        return ResponseEntity.ok(guestService.getBookingsByEmail(email, pageable));
    }
}
