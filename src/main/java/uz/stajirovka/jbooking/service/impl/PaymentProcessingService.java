package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.component.properties.BookingProperties;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.BookingConfirmRequest;
import uz.stajirovka.jbooking.dto.request.PaymentRequest;
import uz.stajirovka.jbooking.dto.response.PaymentResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.PaymentTransactionEntity;
import uz.stajirovka.jbooking.exception.ConflictException;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.mapper.PaymentMapper;
import uz.stajirovka.jbooking.repository.BookingRepository;
import uz.stajirovka.jbooking.repository.PaymentTransactionRepository;
import uz.stajirovka.jbooking.service.CurrencyConverterService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentProcessingService {

    private final BookingRepository bookingRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentMapper paymentMapper;
    private final BookingProperties bookingProperties;
    private final CurrencyConverterService currencyConverter;

    // Фаза 1: короткая транзакция — lock, валидация, статус PAYMENT_PROCESSING
    // Возвращает PaymentRequest для вызова платёжки, или null если HOLD просрочен
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentRequest validateAndLock(Long bookingId, BookingConfirmRequest request) {
        BookingEntity booking = bookingRepository.findByIdWithLock(bookingId)
                .orElseThrow(() -> new NotFoundException(Error.BOOKING_NOT_FOUND));

        // подтвердить можно только бронь в статусе HOLD
        if (booking.getStatus() != BookingStatus.HOLD) {
            throw new ConflictException(Error.INVALID_BOOKING_STATUS,
                    "Переход " + booking.getStatus() + " -> CONFIRMED недопустим");
        }

        // проверяем что платёж ещё не инициирован (защита от гонки)
        if (booking.getPaymentId() != null) {
            throw new ConflictException(Error.DUPLICATE_PAYMENT);
        }

        // проверяем что HOLD не просрочен
        LocalDateTime holdExpiry = booking.getCreatedAt()
                .plusMinutes(bookingProperties.getHoldTimeoutMinutes());
        if (LocalDateTime.now().isAfter(holdExpiry)) {
            booking.setStatus(BookingStatus.CANCELLED);
            booking.setUpdatedAt(LocalDateTime.now());
            return null;
        }

        // сверяем сумму оплаты с totalPrice (конвертируем totalPrice в валюту запроса)
        Long expectedAmount = currencyConverter.convert(booking.getTotalPrice(), request.currency());
        if (!request.amount().equals(expectedAmount)) {
            throw new ConflictException(Error.PAYMENT_AMOUNT_MISMATCH,
                    "Ожидается: " + expectedAmount + " " + request.currency() + ", получено: " + request.amount());
        }

        // фиксируем что платёж инициирован — второй поток увидит не HOLD
        booking.setStatus(BookingStatus.PAYMENT_PROCESSING);
        booking.setUpdatedAt(LocalDateTime.now());

        return paymentMapper.map(request, booking);
    }

    // Фаза 3: короткая транзакция — сохраняем результат платежа
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BookingStatus savePaymentResult(Long bookingId, PaymentResponse paymentResponse) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(Error.BOOKING_NOT_FOUND));

        // сохраняем аудит-лог платежа
        PaymentTransactionEntity paymentLog = PaymentTransactionEntity.builder()
                .transactionId(paymentResponse.id())
                .booking(booking)
                .referenceId(paymentResponse.referenceId())
                .status(paymentResponse.status().name())
                .amount(paymentResponse.amount())
                .currency(paymentResponse.currency().name())
                .transactionCreatedAt(paymentResponse.createdAt())
                .createdAt(LocalDateTime.now())
                .build();
        paymentTransactionRepository.save(paymentLog);

        // сохраняем реальный ID транзакции из платёжного сервиса
        booking.setPaymentId(paymentResponse.id());

        if (BookingStatus.isPaymentSuccessful(paymentResponse.status())) {
            booking.setStatus(BookingStatus.CONFIRMED);
        } else {
            // платёж не прошёл — возвращаем в HOLD
            booking.setStatus(BookingStatus.HOLD);
            booking.setPaymentId(null);
        }

        booking.setUpdatedAt(LocalDateTime.now());

        return booking.getStatus();
    }
}
