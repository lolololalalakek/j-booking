package uz.stajirovka.jbooking.component.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.stajirovka.jbooking.component.properties.BookingProperties;
import uz.stajirovka.jbooking.component.scheduler.helper.HoldExpirationService;
import uz.stajirovka.jbooking.entity.BookingEntity;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingHoldScheduler {

    private final HoldExpirationService holdExpirationService;
    private final BookingProperties bookingProperties;

    @Scheduled(fixedDelayString = "${booking.hold-check-interval-ms}")
    public void cancelExpiredHolds() {
        LocalDateTime expiredBefore = LocalDateTime.now()
            .minusMinutes(bookingProperties.getHoldTimeoutMinutes());

        int totalCancelled = 0;
        boolean hasMore = true;

        while (hasMore) {
            try {
                Slice<BookingEntity> slice = holdExpirationService.cancelExpiredBatch(
                    expiredBefore, bookingProperties.getBatchSize());
                totalCancelled += slice.getNumberOfElements();
                hasMore = slice.hasNext();
            } catch (Exception e) {
                log.error("Ошибка при отмене просроченных HOLD-бронирований, батч прерван", e);
                break;
            }
        }

        if (totalCancelled > 0) {
            log.info("Отменено {} просроченных бронирований", totalCancelled);
        }
    }
}
