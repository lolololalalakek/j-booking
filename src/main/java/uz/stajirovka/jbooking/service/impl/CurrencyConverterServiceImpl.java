package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.stajirovka.jbooking.component.properties.CurrencyProperties;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.service.CurrencyConverterService;

@Service
@RequiredArgsConstructor
public class CurrencyConverterServiceImpl implements CurrencyConverterService {

    private final CurrencyProperties currencyProperties;

    @Override
    public Long convert(Long amountInUzs, Currency currency) {
        if (amountInUzs == null || currency == Currency.UZS) {
            return amountInUzs;
        }

        return switch (currency) {
            case USD -> amountInUzs / currencyProperties.getUsdRate();
            case RUB -> amountInUzs / currencyProperties.getRubRate();
            default -> amountInUzs;
        };
    }
}
