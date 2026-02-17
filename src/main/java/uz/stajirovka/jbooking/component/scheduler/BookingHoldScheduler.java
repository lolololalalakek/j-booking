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
public class BookingHoldScheduler {

    private static final int BATCH_SIZE = 100;

    private final BookingRepository bookingRepository;
    private final BookingProperties bookingProperties;

    @Scheduled(fixedDelayString = "${booking.hold-check-interval-ms:30000}")
    @Transactional
    public void cancelExpiredHolds() {
        LocalDateTime expiredBefore = LocalDateTime.now()
                .minusMinutes(bookingProperties.getHoldTimeoutMinutes());

        int totalCancelled = 0;
        Slice<BookingEntity> slice;

        do {
            // всегда берём page 0, потому что после обновления статуса
            // предыдущие записи уже не попадут в выборку
            slice = bookingRepository.findExpiredHolds(
                    BookingStatus.HOLD, expiredBefore, PageRequest.of(0, BATCH_SIZE));

            LocalDateTime now = LocalDateTime.now();
            for (BookingEntity booking : slice.getContent()) {
                booking.setStatus(BookingStatus.CANCELLED);
                booking.setUpdatedAt(now);
                log.info("HOLD таймаут: бронирование id={} отменено (создано {})",
                        booking.getId(), booking.getCreatedAt());
            }

            totalCancelled += slice.getNumberOfElements();
        } while (slice.hasNext());

        if (totalCancelled > 0) {
            log.info("Отменено {} просроченных бронирований", totalCancelled);
        }
    }
}
