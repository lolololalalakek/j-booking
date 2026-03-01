package uz.stajirovka.jbooking.component.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "booking")
public class BookingProperties {

    // минимальное количество ночей
    private int minNights;

    // таймаут удержания брони в минутах (HOLD -> CANCELLED)
    private int holdTimeoutMinutes;

    // интервал проверки просроченных HOLD бронирований (мс)
    private int holdCheckIntervalMs;

    // тайм-аут для статуса CONFIRMED (мин) — после него шедулер сбрасывает обратно в HOLD
    private int confirmedTimeoutMinutes;

    // интервал проверки просроченных CONFIRMED бронирований (мс)
    private int confirmedCheckIntervalMs;

    // размер батча при пакетной обработке бронирований в шедулерах
    private int batchSize;
}