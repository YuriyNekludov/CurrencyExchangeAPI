package edu.project.services;

import edu.project.dto.CurrencyDto;
import edu.project.models.Currency;

import java.util.List;

public interface Service<T> {

    T addElement(String value1, String value2, String value3);

    T getElementByCode(String code);

    List<T> getAllElements();

    default CurrencyDto mapCurrencyToDto(Currency currency) {
        return new CurrencyDto(
                currency.getId(),
                currency.getCode(),
                currency.getFullName(),
                currency.getSign());
    }

}
