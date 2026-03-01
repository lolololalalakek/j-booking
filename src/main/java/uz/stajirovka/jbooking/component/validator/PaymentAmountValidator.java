package uz.stajirovka.jbooking.component.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.BookingPaymentRequest;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.exception.ConflictException;
import uz.stajirovka.jbooking.service.CurrencyConverterService;

@Component
@RequiredArgsConstructor
public class PaymentAmountValidator {

    private final CurrencyConverterService currencyConverter;

    public boolean isValidBooking(BookingEntity booking, BookingPaymentRequest request) {
        Long expectedAmount = currencyConverter.convert(booking.getTotalPrice(), request.currency());
        if (!request.amount().equals(expectedAmount)) {
            throw new ConflictException(Error.PAYMENT_AMOUNT_MISMATCH,
                "Expected: " + expectedAmount + " " + request.currency() + ", got: " + request.amount());
        }
        booking.setStatus(BookingStatus.PAID);
        return true;
    }
}
