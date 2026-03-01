package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.stajirovka.jbooking.component.validator.PaymentAmountValidator;
import uz.stajirovka.jbooking.dto.request.BookingPaymentRequest;
import uz.stajirovka.jbooking.dto.request.PaymentRequest;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.mapper.PaymentMapper;

@Service
@RequiredArgsConstructor
public class PaymentRequestValidationService {

    private final PaymentMapper paymentMapper;
    private final PaymentAmountValidator paymentAmountValidator;

    public PaymentRequest validate(BookingEntity booking, BookingPaymentRequest request) {

        if (!paymentAmountValidator.isValidBooking(booking, request)) {
            return null;
        }

        return paymentMapper.map(request);
    }
}
