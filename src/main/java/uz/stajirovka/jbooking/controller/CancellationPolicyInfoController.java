package uz.stajirovka.jbooking.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.stajirovka.jbooking.dto.response.CancellationPolicyInfoResponse;
import uz.stajirovka.jbooking.service.CancellationPolicyService;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Validated
public class CancellationPolicyInfoController {

    private final CancellationPolicyService cancellationPolicyService;

    @GetMapping("/{id}/refund-info")
    public ResponseEntity<CancellationPolicyInfoResponse> getRefundInfo(@PathVariable("id") @Positive Long bookingId) {
        return ResponseEntity.ok(cancellationPolicyService.getCancellationPolicyInfo(bookingId));
    }
}
