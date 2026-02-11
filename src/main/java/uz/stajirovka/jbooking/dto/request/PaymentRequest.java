package uz.stajirovka.jbooking.dto.request;

import java.math.BigDecimal;

public record PaymentRequest(
    Long totalPrice,
    Long cardId
) {
}
