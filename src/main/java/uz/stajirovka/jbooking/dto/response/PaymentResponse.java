package uz.stajirovka.jbooking.dto.response;

import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.constant.enums.TransactionStatus;

public record PaymentResponse(

    TransactionStatus transactionStatus,
    Long transactionId
) {
}
