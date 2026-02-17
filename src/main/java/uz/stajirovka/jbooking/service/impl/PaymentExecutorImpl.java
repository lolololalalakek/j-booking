package uz.stajirovka.jbooking.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import uz.stajirovka.jbooking.component.adapter.PaymentClient;
import uz.stajirovka.jbooking.dto.request.PaymentRequest;
import uz.stajirovka.jbooking.dto.response.PaymentResponse;
import uz.stajirovka.jbooking.service.PaymentExecutor;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentExecutorImpl implements PaymentExecutor {



    PaymentClient paymentClient;


    @Override
    public PaymentResponse executePayment(PaymentRequest paymentRequest) {
        return paymentClient.executePayment(paymentRequest);
    }
}
