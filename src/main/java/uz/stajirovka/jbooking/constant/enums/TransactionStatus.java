package uz.stajirovka.jbooking.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionStatus {
    CREATED,
    SENDER_INFO_VALIDATED,
    SENDER_INFO_VALIDATION_FAILED,
    RECEIVER_INFO_VALIDATED,
    RECEIVER_INFO_VALIDATION_FAILED,
    AMOUNT_VALIDATED,
    AMOUNT_VALIDATION_FAILED,
    CALCULATE_FAILED,
    SENT_TO_CORE_LEDGER,
    SUCCESS,
    FAILED
}

