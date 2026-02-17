package uz.stajirovka.jbooking.component.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "booking")
public class BookingProperties {

    // минимальное количество ночей
    private int minNights = 1;

    // количество основных гостей (всегда 1)
    private int mainGuestCount = 1;

    // таймаут удержания брони в минутах (HOLD -> CANCELLED)
    private int holdTimeoutMinutes = 2;
}
