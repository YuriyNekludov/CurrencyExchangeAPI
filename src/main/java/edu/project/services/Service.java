package edu.project.services;

import edu.project.dto.CurrencyDto;
import edu.project.models.Currency;

import java.util.List;

public interface Service<T> {

    T getElementByCode(String code);

    List<T> getAllElements();

    default CurrencyDto mapCurrencyToDto(Currency currency) {
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId(currency.getId());
        currencyDto.setCode(currency.getCode());
        currencyDto.setFullName(currency.getFullName());
        currencyDto.setSign(currency.getSign());
        return currencyDto;
    }

}
