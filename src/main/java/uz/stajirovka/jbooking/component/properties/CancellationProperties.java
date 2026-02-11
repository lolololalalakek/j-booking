package uz.stajirovka.jbooking.component.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "booking.cancellation")
public class CancellationProperties {

    // часы до заезда для полного возврата
    private int fullRefundHours = 72;

    // часы до заезда для частичного возврата
    private int partialRefundHours = 24;

    // процент полного возврата
    private int fullRefundPercent = 100;

    // процент частичного возврата
    private int partialRefundPercent = 50;

    // процент без возврата
    private int noRefundPercent = 0;
}
