package uz.stajirovka.jbooking.component.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.stajirovka.jbooking.component.properties.BookingProperties;
import uz.stajirovka.jbooking.component.scheduler.helper.ConfirmedExpirationService;
import uz.stajirovka.jbooking.entity.BookingEntity;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingConfirmedScheduler {

    private final ConfirmedExpirationService confirmedExpirationService;
    private final BookingProperties bookingProperties;

    @Scheduled(fixedDelayString = "${booking.confirmed-check-interval-ms}")
    public void resetExpiredConfirmed() {
        LocalDateTime expiredBefore = LocalDateTime.now()
            .minusMinutes(bookingProperties.getConfirmedTimeoutMinutes());

        int totalReset = 0;
        boolean hasMore = true;

        while (hasMore) {
            try {
                Slice<BookingEntity> slice = confirmedExpirationService.resetExpiredConfirmedBatch(
                    expiredBefore, bookingProperties.getBatchSize());
                totalReset += slice.getNumberOfElements();
                hasMore = slice.hasNext();
            } catch (Exception e) {
                log.error("Ошибка при сбросе просроченных CONFIRMED-бронирований, батч прерван", e);
                break;
            }
        }

        if (totalReset > 0) {
            log.info("Сброшено {} просроченных CONFIRMED-бронирований обратно в HOLD", totalReset);
        }
    }
}
