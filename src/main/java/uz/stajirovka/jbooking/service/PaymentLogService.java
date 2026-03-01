package uz.stajirovka.jbooking.service;

import uz.stajirovka.jbooking.dto.response.PaymentResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;

public interface PaymentLogService {

    void log(BookingEntity booking, PaymentResponse paymentResponse);
}