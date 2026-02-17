package uz.stajirovka.jbooking.component.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "currency")
public class CurrencyProperties {

    private long usdRate;
    private long rubRate;
}
