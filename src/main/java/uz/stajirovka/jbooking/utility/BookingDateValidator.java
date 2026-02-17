package uz.stajirovka.jbooking.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.exception.ValidationException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingDateValidator {

    public static long validateAndCalculateNights(LocalDateTime checkIn, LocalDateTime checkOut, int minNights) {
        if (!checkOut.isAfter(checkIn)) {
            throw new ValidationException(Error.VALIDATION_ERROR, "Дата выезда должна быть после даты заезда");
        }

        long nights = ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate());

        if (nights < minNights) {
            throw new ValidationException(Error.VALIDATION_ERROR,
                    "Минимальный срок проживания — " + minNights + " ночь");
        }

        return nights;
    }

    public static void validateDates(LocalDateTime checkIn, LocalDateTime checkOut) {
        if (!checkOut.isAfter(checkIn)) {
            throw new ValidationException(Error.VALIDATION_ERROR, "Дата выезда должна быть после даты заезда");
        }
    }
}
