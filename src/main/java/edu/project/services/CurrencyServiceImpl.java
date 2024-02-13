package edu.project.services;

import edu.project.dao.CurrencyDao;
import edu.project.dao.CurrencyJdbcDao;
import edu.project.dto.CurrencyDto;
import edu.project.exceptions.DataAlreadyExistException;
import edu.project.exceptions.DataNotFoundException;
import edu.project.exceptions.InternalServerException;
import edu.project.models.Currency;
import edu.project.utils.ParameterValidator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyServiceImpl implements CurrencyService {
    private static final CurrencyServiceImpl CURRENCY_SERVICE_INSTANCE = new CurrencyServiceImpl();
    private final CurrencyDao currencyDao = CurrencyJdbcDao.getCurrencyDao();

    private CurrencyServiceImpl() {
    }

    public static CurrencyServiceImpl getCurrencyService() {
        return CURRENCY_SERVICE_INSTANCE;
    }

    @Override
    public CurrencyDto addElement(String code, String name, String sign) {
        ParameterValidator.areValidCurrencyParameters(code, name, sign);
        code = code.toUpperCase();
        try {
            Currency currency = currencyDao.addElement(new Currency(code.toUpperCase(), name, sign));
            return mapCurrencyToDto(currency);
        } catch (SQLException e) {
            if (e.getErrorCode() == 19)
                throw new DataAlreadyExistException("Валюта с таким кодом уже существует.");
            else
                throw new InternalServerException("База данных недоступна.");
        }
    }

    @Override
    public CurrencyDto getElementByCode(String code) {
        ParameterValidator.isValidCode(code);
        code = code.toUpperCase();
        try {
            Optional<Currency> currency = currencyDao.getElementByCode(code);
            if (currency.isEmpty())
                throw new DataNotFoundException("Валюта не найдена.");
            return mapCurrencyToDto(currency.get());
        } catch (SQLException e) {
            throw new InternalServerException("База данных недоступна.");
        }
    }

    @Override
    public List<CurrencyDto> getAllElements() {
        try {
            List<CurrencyDto> currenciesDto = new ArrayList<>();
            List<Currency> currencies = currencyDao.getAllElements();
            if (currencies.isEmpty())
                throw new DataNotFoundException("Валюты отсутствуют в базе данных.");
            currencies.forEach(currency -> {
                CurrencyDto currencyDto = mapCurrencyToDto(currency);
                currenciesDto.add(currencyDto);
            });
            return currenciesDto;
        } catch (SQLException e) {
            throw new InternalServerException("База данных недоступна.");
        }
    }
}
