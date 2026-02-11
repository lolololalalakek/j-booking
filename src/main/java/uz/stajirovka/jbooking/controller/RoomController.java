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
import uz.stajirovka.jbooking.dto.request.RoomCreateRequest;
import uz.stajirovka.jbooking.dto.request.RoomSearchRequest;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.service.RoomSearchService;
import uz.stajirovka.jbooking.service.RoomService;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomSearchService roomSearchService;

    @PostMapping
    public ResponseEntity<RoomResponse> create(@Valid @RequestBody RoomCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Slice<RoomResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(roomService.getAll(pageable));
    }

    @PostMapping("/search")
    public ResponseEntity<Slice<RoomResponse>> search(
            @Valid @RequestBody RoomSearchRequest request, Pageable pageable) {
        return ResponseEntity.ok(roomSearchService.search(request, pageable));
    }

    @GetMapping("/by-hotel/{hotelId}")
    public ResponseEntity<Slice<RoomResponse>> getByHotelId(@PathVariable Long hotelId,
                                                             Pageable pageable) {
        return ResponseEntity.ok(roomService.getByHotelId(hotelId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> update(
        @PathVariable Long id,
        @Valid @RequestBody RoomCreateRequest request) {
        return ResponseEntity.ok(roomService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
