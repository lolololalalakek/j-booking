package uz.stajirovka.jbooking.service;

import uz.stajirovka.jbooking.dto.request.BookingConfirmRequest;
import uz.stajirovka.jbooking.dto.request.PaymentRequest;
import uz.stajirovka.jbooking.dto.response.PaymentResponse;

import java.math.BigDecimal;

public interface PaymentExecutor {

    PaymentResponse executePayment (BigDecimal totalPrice, Long cardId);


}
