package uz.stajirovka.jbooking.service;

import uz.stajirovka.jbooking.constant.enums.Currency;

public interface CurrencyConverterService {

    Long convert(Long amountInUzs, Currency currency);
}
