package uz.stajirovka.jbooking.dto.response;

import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.constant.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(

    Long id,
    UUID referenceId,
    TransactionStatus status,
    Long amount,
    Currency currency,
    LocalDateTime createdAt
) {
}
