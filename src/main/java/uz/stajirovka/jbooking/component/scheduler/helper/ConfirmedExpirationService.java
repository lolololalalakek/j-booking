package uz.stajirovka.jbooking.component.scheduler.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.repository.BookingRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmedExpirationService {

    private final BookingRepository bookingRepository;

    @Transactional
    public Slice<BookingEntity> resetExpiredConfirmedBatch(LocalDateTime expiredBefore, int batchSize) {
        Slice<BookingEntity> slice = bookingRepository.findExpiredConfirmed(
            BookingStatus.CONFIRMED, expiredBefore, PageRequest.of(0, batchSize));

        for (BookingEntity booking : slice.getContent()) {
            booking.setStatus(BookingStatus.HOLD);
            log.info("CONFIRMED таймаут: бронирование id={} сброшено обратно в HOLD (подтверждено {})",
                booking.getId(), booking.getUpdatedAt());
        }

        bookingRepository.saveAll(slice.getContent());

        return slice;
    }
}
