package edu.project.services;

import edu.project.dto.ExchangeRateDto;

import java.math.BigDecimal;

public interface ExchangeRateService extends Service<ExchangeRateDto> {

    ExchangeRateDto getExchangeRateForConversion(String baseCode, String targetCode);

    ExchangeRateDto updateElement(String code, String rate);

    BigDecimal amountExchange(ExchangeRateDto rate, String amount);
}
