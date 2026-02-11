package uz.stajirovka.jbooking.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionStatus {
    CREATED(false),
    SENDER_INFO_VALIDATED(false),
    SENDER_INFO_VALIDATION_FAILED(true),
    RECEIVER_INFO_VALIDATED(false),
    RECEIVER_INFO_VALIDATION_FAILED(true),
    AMOUNT_VALIDATED(false),
    AMOUNT_VALIDATION_FAILED(true),
    CALCULATE_FAILED(false),
    PROCESSING_BY_LEDGER(false),
    COMPLETED(true),
    FAILED(true);

    private final boolean isFailed;
}

