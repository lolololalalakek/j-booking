package uz.stajirovka.jbooking.dto.response;

public record CancellationPolicyInfoResponse(
    Long refundAmount,
    int refundPercent
) {
}
