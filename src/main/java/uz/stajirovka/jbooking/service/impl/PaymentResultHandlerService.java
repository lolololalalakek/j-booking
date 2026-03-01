package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.constant.enums.TransactionStatus;
import uz.stajirovka.jbooking.dto.response.PaymentResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.service.PaymentLogService;

@Service
@RequiredArgsConstructor
public class PaymentResultHandlerService {

    private final PaymentLogService paymentLogService;

    public BookingEntity handle(BookingEntity booking, PaymentResponse paymentResponse) {

        paymentLogService.log(booking, paymentResponse);

        booking.setPaymentId(paymentResponse.id());

        if (paymentResponse.status() == TransactionStatus.SUCCESS) {
            booking.setStatus(BookingStatus.PAID);
            booking.setPaymentId(paymentResponse.id());
        } else {
            booking.setStatus(BookingStatus.HOLD);
            booking.setPaymentId(null);
        }

        return booking;
    }
}
