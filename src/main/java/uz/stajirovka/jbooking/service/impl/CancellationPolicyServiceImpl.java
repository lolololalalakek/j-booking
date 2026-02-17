package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.stajirovka.jbooking.component.properties.CancellationProperties;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.service.CancellationPolicyService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CancellationPolicyServiceImpl implements CancellationPolicyService {

    private final CancellationProperties cancellationProperties;

    @Override
    public Long calculateRefund(BookingEntity booking) {
        if (booking.getTotalPrice() == null) {
            return 0L;
        }

        long hoursUntilCheckIn = ChronoUnit.HOURS.between(LocalDateTime.now(), booking.getCheckInDate());

        // полный возврат
        if (hoursUntilCheckIn >= cancellationProperties.getFullRefundHours()) {
            return booking.getTotalPrice();
        }

        // частичный возврат
        if (hoursUntilCheckIn >= cancellationProperties.getPartialRefundHours()) {
            return booking.getTotalPrice() * cancellationProperties.getPartialRefundPercent() / 100;
        }

        // без возврата
        return 0L;
    }

    @Override
    public int getRefundPercent(LocalDateTime checkInDate) {
        long hoursUntilCheckIn = ChronoUnit.HOURS.between(LocalDateTime.now(), checkInDate);

        if (hoursUntilCheckIn >= cancellationProperties.getFullRefundHours()) {
            return cancellationProperties.getFullRefundPercent();
        }
        if (hoursUntilCheckIn >= cancellationProperties.getPartialRefundHours()) {
            return cancellationProperties.getPartialRefundPercent();
        }
        return cancellationProperties.getNoRefundPercent();
    }
}
