package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uz.stajirovka.jbooking.constant.enums.Currency;

public record BookingConfirmRequest(
    @NotNull(message = "bookingId is required")
    Long bookingId,

    @NotNull(message = "amount is required") @Positive(message = "amount should be positive")
    Long amount,
    @NotNull(message = "currency required")
    Currency currency,
    @NotBlank(message = "senderName required")
    String senderName,
    @NotBlank(message = "senderToken required")
    String senderToken,
    @NotBlank(message = "receiverName required")
    String receiverName,
    @NotBlank(message = "receiverToken required")
    String receiverToken
) {
}
