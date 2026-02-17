package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.constant.enums.TransactionType;

public record PaymentRequest(
    @NotNull(message = "referenceId required") Long referenceId,
    @NotNull(message = "transaction type required") TransactionType type,
    @NotNull(message = "amount is required") @Positive(message = "amount should be positive") Long amount,
    @NotNull(message = "currency required") Currency currency,
    @NotBlank(message = "senderName required") String senderName,
    @NotBlank(message = "senderToken required") String senderToken,
    @NotBlank(message = "receiverName required") String receiverName,
    @NotBlank(message = "receiverToken required") String receiverToken
) {
}
