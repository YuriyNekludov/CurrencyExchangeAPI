package edu.project.services;

import edu.project.dao.CurrencyDao;
import edu.project.dao.CurrencyJdbcDao;
import edu.project.dao.ExchangeRateDao;
import edu.project.dao.ExchangeRateJdbcDao;
import edu.project.dto.CurrencyDto;
import edu.project.dto.ExchangeRateDto;
import edu.project.exceptions.DataAlreadyExistException;
import edu.project.exceptions.DataNotFoundException;
import edu.project.exceptions.InternalServerException;
import edu.project.models.Currency;
import edu.project.models.ExchangeRate;
import edu.project.utils.ParameterValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private static final ExchangeRateServiceImpl EXCHANGE_RATE_SERVICE_INSTANCE = new ExchangeRateServiceImpl();
    private final ExchangeRateDao rateDao = ExchangeRateJdbcDao.getExchangeRateDao();
    private final CurrencyDao currencyDao = CurrencyJdbcDao.getCurrencyDao();

    private ExchangeRateServiceImpl() {
    }

    public static ExchangeRateServiceImpl getExchangeRateService() {
        return EXCHANGE_RATE_SERVICE_INSTANCE;
    }

    @Override
    public ExchangeRateDto getExchangeRateForConversion(String baseCode, String targetCode) {
        ParameterValidator.areValidRateParameters(baseCode, targetCode);
        baseCode = baseCode.toUpperCase();
        targetCode = targetCode.toUpperCase();
        try {
            Optional<ExchangeRate> exchangeRate = rateDao.getElementByCode(baseCode, targetCode);
            if (exchangeRate.isPresent())
                return mapExchangeRateToDto(exchangeRate.get());
            else {
                Optional<ExchangeRate> reverseRate = rateDao.getElementByCode(targetCode, baseCode);
                if (reverseRate.isPresent()) {
                    ExchangeRateDto reverseDto = mapExchangeRateToDto(reverseRate.get());
                    CurrencyDto baseReverse = reverseDto.getTargetCurrency();
                    CurrencyDto targetReverse = reverseDto.getBaseCurrency();
                    reverseDto.setBaseCurrency(baseReverse);
                    reverseDto.setTargetCurrency(targetReverse);
                    reverseDto.setRate(BigDecimal.ONE
                            .setScale(6, RoundingMode.HALF_UP)
                            .divide(reverseDto.getRate(), RoundingMode.HALF_UP));
                    return reverseDto;
                } else
                    return getCrossCourseRate(baseCode, targetCode);
            }
        } catch (SQLException e) {
            throw new InternalServerException("База данных недоступна.");
        }
    }

    @Override
    public BigDecimal amountExchange(ExchangeRateDto rate, String amount) {
        BigDecimal amountValue = new BigDecimal(amount);
        return rate.getRate().setScale(6, RoundingMode.HALF_UP).multiply(amountValue);
    }

    @Override
    public ExchangeRateDto updateElement(String code, String rate) {
        ParameterValidator.areValidRateParameters(code, rate);
        code = code.toUpperCase();
        try {
            ExchangeRateDto exchangeRate = getElementByCode(code);
            BigDecimal newRate = new BigDecimal(rate);
            exchangeRate.setRate(newRate);
            rateDao.updateElement(exchangeRate);
            return exchangeRate;
        } catch (SQLException e) {
            throw new InternalServerException("База данных недоступна.");
        }
    }

    @Override
    public ExchangeRateDto addElement(String baseCode, String targetCode, String rate) {
        ParameterValidator.areValidRateParameters(baseCode, targetCode, rate);
        baseCode = baseCode.toUpperCase();
        targetCode = targetCode.toUpperCase();
        try {
            Optional<Currency> base = currencyDao.getElementByCode(baseCode);
            Optional<Currency> target = currencyDao.getElementByCode(targetCode);
            BigDecimal newRate = new BigDecimal(rate);
            if (base.isEmpty() || target.isEmpty())
                throw new DataNotFoundException("Одна или обе валюты из валютной пары не найдены.");
            ExchangeRate exchangeRate = rateDao.addElement(new ExchangeRate(base.get(), target.get(), newRate));
            return mapExchangeRateToDto(exchangeRate);
        } catch (SQLException e) {
            if (e.getErrorCode() == 19)
                throw new DataAlreadyExistException("Валютная пара с таким кодом уже существует.");
            throw new InternalServerException("База данных недоступна.");
        }
    }

    @Override
    public ExchangeRateDto getElementByCode(String code) {
        ParameterValidator.isValidCode(code);
        code = code.toUpperCase();
        try {
            String baseCode = code.substring(0, 3);
            String targetCode = code.substring(3);
            Optional<ExchangeRate> exchangeRate = rateDao.getElementByCode(baseCode, targetCode);
            if (exchangeRate.isEmpty())
                throw new DataNotFoundException("Валютная пара не найдена.");
            return mapExchangeRateToDto(exchangeRate.get());
        } catch (SQLException e) {
            throw new InternalServerException("База данных недоступна.");
        }
    }

    @Override
    public List<ExchangeRateDto> getAllElements() {
        try {
            List<ExchangeRate> exchangeRates = rateDao.getAllElements();
            if (exchangeRates.isEmpty())
                throw new DataNotFoundException("Валютные пары отсутствуют в базе данных.");
            List<ExchangeRateDto> rateDtoList = new ArrayList<>();
            exchangeRates.forEach(exchangeRate -> {
                ExchangeRateDto exchangeRateDto = mapExchangeRateToDto(exchangeRate);
                rateDtoList.add(exchangeRateDto);
            });
            return rateDtoList;
        } catch (SQLException e) {
            throw new InternalServerException("База данных недоступна.");
        }
    }

    private ExchangeRateDto getCrossCourseRate(String baseCode, String targetCode) throws SQLException {
        List<ExchangeRate> crossCourse = rateDao.getExchangeRateByCrossCourse(baseCode, targetCode);
        if (crossCourse.isEmpty())
            throw new DataNotFoundException("Валютные пары отсутствуют в базе данных.");
        ExchangeRate baseRate;
        ExchangeRate targetRate;
        if (crossCourse.get(0).getTargetCurrency().getCode().equals(baseCode)) {
            baseRate = crossCourse.get(0);
            targetRate = crossCourse.get(1);
        } else {
            baseRate = crossCourse.get(1);
            targetRate = crossCourse.get(0);
        }
        return  getCrossCourse(baseRate, targetRate);
    }

    private ExchangeRateDto getCrossCourse(ExchangeRate baseRate, ExchangeRate targetRate) {
        ExchangeRate crossRate = new ExchangeRate(
                    baseRate.getTargetCurrency(),
                    targetRate.getTargetCurrency(),
                    baseRate.getRate()
                            .setScale(6, RoundingMode.HALF_UP)
                            .divide(targetRate.getRate(), RoundingMode.HALF_UP));
        return mapExchangeRateToDto(crossRate);
    }

    private ExchangeRateDto mapExchangeRateToDto(ExchangeRate rate) {
        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        CurrencyDto base = mapCurrencyToDto(rate.getBaseCurrency());
        CurrencyDto target = mapCurrencyToDto(rate.getTargetCurrency());
        exchangeRateDto.setId(rate.getId());
        exchangeRateDto.setBaseCurrency(base);
        exchangeRateDto.setTargetCurrency(target);
        exchangeRateDto.setRate(rate.getRate());
        return exchangeRateDto;
    }
}
