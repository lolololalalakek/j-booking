package uz.stajirovka.jbooking.service;

import uz.stajirovka.jbooking.dto.request.PaymentRequest;
import uz.stajirovka.jbooking.dto.response.PaymentResponse;

public interface PaymentExecutor {

    PaymentResponse executePayment(PaymentRequest paymentRequest);
}
