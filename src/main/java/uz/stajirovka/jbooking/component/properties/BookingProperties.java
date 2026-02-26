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

    // таймаут для статуса PAYMENT_PROCESSING (мин) — после него recovery-шедулер сбрасывает в HOLD
    private int paymentProcessingTimeoutMinutes;

    // интервал проверки застрявших PAYMENT_PROCESSING бронирований (мс)
    private int paymentProcessingCheckIntervalMs;

    // размер батча при пакетной обработке бронирований в шедулерах
    private int batchSize;
}