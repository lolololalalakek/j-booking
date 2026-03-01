package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.stajirovka.jbooking.dto.response.PaymentResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.PaymentTransactionEntity;
import uz.stajirovka.jbooking.mapper.PaymentTransactionMapper;
import uz.stajirovka.jbooking.repository.PaymentTransactionRepository;
import uz.stajirovka.jbooking.service.PaymentLogService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

// сохраняем информацию о платеже в бд. просто информация о том, что бронирование оплачено

public class PaymentLogServiceImpl implements PaymentLogService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentTransactionMapper paymentTransactionMapper;

    @Override
    public void log(BookingEntity booking, PaymentResponse paymentResponse) {
        PaymentTransactionEntity paymentLog = paymentTransactionMapper.toEntity(paymentResponse, booking, LocalDateTime.now());
        paymentTransactionRepository.save(paymentLog);
    }
}