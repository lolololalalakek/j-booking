package uz.stajirovka.jbooking.component.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.stajirovka.jbooking.component.properties.BookingProperties;
import uz.stajirovka.jbooking.component.scheduler.helper.PaymentRecoveryService;
import uz.stajirovka.jbooking.entity.BookingEntity;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProcessingRecoveryScheduler {

    private final PaymentRecoveryService paymentRecoveryService;
    private final BookingProperties bookingProperties;

    @Scheduled(fixedDelayString = "${booking.payment-processing-check-interval-ms}")
    public void recoverStuckPaymentProcessing() {
        LocalDateTime stuckBefore = LocalDateTime.now()
            .minusMinutes(bookingProperties.getPaymentProcessingTimeoutMinutes());

        int totalRecovered = 0;
        boolean hasMore = true;

        while (hasMore) {
            try {
                Slice<BookingEntity> slice = paymentRecoveryService.recoverStuckBatch(
                    stuckBefore, bookingProperties.getBatchSize());
                totalRecovered += slice.getNumberOfElements();
                hasMore = slice.hasNext();
            } catch (Exception e) {
                log.error("Ошибка при восстановлении зависших PAYMENT_PROCESSING бронирований, батч прерван", e);
                break;
            }
        }

        if (totalRecovered > 0) {
            log.info("Recovery: {} бронирований сброшено из PAYMENT_PROCESSING в HOLD", totalRecovered);
        }
    }
}
