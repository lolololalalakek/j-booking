package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.stajirovka.jbooking.component.properties.CurrencyProperties;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.service.CurrencyConverterService;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CurrencyConverterServiceImpl implements CurrencyConverterService {

    private final CurrencyProperties currencyProperties;

    @Override
    public Long convert(Long amountInTiyins, Currency currency) {
        if (amountInTiyins == null || currency == Currency.UZS) {
            return amountInTiyins;
        }

        return switch (currency) {
            case USD -> divide(amountInTiyins, currencyProperties.getUsdRate());
            case RUB -> divide(amountInTiyins, currencyProperties.getRubRate());
            default -> amountInTiyins;
        };
    }

    private long divide(long amount, long rate) {
        return BigDecimal.valueOf(amount)
                .divide(BigDecimal.valueOf(rate), 0, RoundingMode.HALF_UP)
                .longValue();
    }
}
