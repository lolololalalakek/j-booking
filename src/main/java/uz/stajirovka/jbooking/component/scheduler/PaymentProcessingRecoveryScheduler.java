package uz.stajirovka.jbooking.component.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.component.properties.BookingProperties;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.repository.BookingRepository;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProcessingRecoveryScheduler {

    private static final int BATCH_SIZE = 100;

    private final BookingRepository bookingRepository;
    private final BookingProperties bookingProperties;

    @Scheduled(fixedDelayString = "${booking.payment-processing-check-interval-ms:60000}")
    @Transactional
    public void recoverStuckPaymentProcessing() {
        LocalDateTime stuckBefore = LocalDateTime.now()
                .minusMinutes(bookingProperties.getPaymentProcessingTimeoutMinutes());

        int totalRecovered = 0;
        Slice<BookingEntity> slice;

        do {
            // всегда берём page 0 — после обновления статуса записи выпадают из выборки
            slice = bookingRepository.findStuckPaymentProcessing(
                    BookingStatus.PAYMENT_PROCESSING, stuckBefore, PageRequest.of(0, BATCH_SIZE));

            LocalDateTime now = LocalDateTime.now();
            for (BookingEntity booking : slice.getContent()) {
                booking.setStatus(BookingStatus.HOLD);
                booking.setUpdatedAt(now);
                log.warn("Recovery: бронирование id={} застряло в PAYMENT_PROCESSING, сброшено в HOLD",
                        booking.getId());
            }

            totalRecovered += slice.getNumberOfElements();
        } while (slice.hasNext());

        if (totalRecovered > 0) {
            log.info("Recovery: {} бронирований сброшено из PAYMENT_PROCESSING в HOLD", totalRecovered);
        }
    }
}
