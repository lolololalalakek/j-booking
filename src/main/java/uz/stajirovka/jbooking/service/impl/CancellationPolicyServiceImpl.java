package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.stajirovka.jbooking.component.properties.CancellationProperties;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.response.CancellationPolicyInfoResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.repository.BookingRepository;
import uz.stajirovka.jbooking.service.CancellationPolicyService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CancellationPolicyServiceImpl implements CancellationPolicyService {

    private final CancellationProperties cancellationProperties;
    private final BookingRepository bookingRepository;

    @Override
    public CancellationPolicyInfoResponse getCancellationPolicyInfo(long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new NotFoundException(Error.BOOKING_NOT_FOUND));

        long hoursUntilCheckIn = ChronoUnit.HOURS.between(LocalDateTime.now(), booking.getCheckInDate());
        int refundPercent = resolveRefundPercent(hoursUntilCheckIn);
        Long refundAmount = resolveRefundAmount(booking.getTotalPrice(), hoursUntilCheckIn);
        return new CancellationPolicyInfoResponse(refundAmount, refundPercent);
    }

    private int resolveRefundPercent(long hoursUntilCheckIn) {
        if (hoursUntilCheckIn >= cancellationProperties.getFullRefundHours()) {
            return cancellationProperties.getFullRefundPercent();
        }
        if (hoursUntilCheckIn >= cancellationProperties.getPartialRefundHours()) {
            return cancellationProperties.getPartialRefundPercent();
        }
        return cancellationProperties.getNoRefundPercent();
    }

    private Long resolveRefundAmount(Long totalPrice, long hoursUntilCheckIn) {
        if (totalPrice == null) {
            return 0L;
        }
        if (hoursUntilCheckIn >= cancellationProperties.getFullRefundHours()) {
            return totalPrice;
        }
        if (hoursUntilCheckIn >= cancellationProperties.getPartialRefundHours()) {
            return totalPrice * cancellationProperties.getPartialRefundPercent() / 100;
        }
        return 0L;
    }
}
