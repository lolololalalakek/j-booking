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
public class HoldExpirationService {

    private final BookingRepository bookingRepository;

    @Transactional
    public Slice<BookingEntity> cancelExpiredBatch(LocalDateTime expiredBefore, int batchSize) {
        Slice<BookingEntity> slice = bookingRepository.findExpiredHolds(
            BookingStatus.HOLD, expiredBefore, PageRequest.of(0, batchSize));

        for (BookingEntity booking : slice.getContent()) {
            booking.setStatus(BookingStatus.CANCELLED);
            log.info("HOLD таймаут: бронирование id={} отменено (создано {})",
                booking.getId(), booking.getCreatedAt());
        }

        return slice;
    }
}
