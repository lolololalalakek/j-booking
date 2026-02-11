package uz.stajirovka.jbooking.component.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "validation")
public class ValidationProperties {

    // звёзды отеля
    private int minStars = 1;
    private int maxStars = 5;

    // рейтинг отзыва
    private int minRating = 1;
    private int maxRating = 5;

    // гости
    private int minGuests = 1;
}
