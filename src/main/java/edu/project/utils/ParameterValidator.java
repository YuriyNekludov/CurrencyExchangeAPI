package edu.project.utils;

import edu.project.exceptions.NoValidParametersException;

public final class ParameterValidator {

    private ParameterValidator() {
    }

    public static void areValidCurrencyParameters(String code, String name, String sign) {
        if (name == null || sign == null || name.isEmpty() || sign.isEmpty() || isInvalidCode(code))
                throw new NoValidParametersException("Введеные поля формы введены некоректно либо отсутствуют.");
    }

    public static void isValidCurrencyCode(String code) {
        if (isInvalidCode(code))
            throw new NoValidParametersException("Введеные поля формы введены некоректно либо отсутствуют.");
    }

    public static void isValidRateCode(String code) {
        if (code == null || code.length() != 6)
            throw new NoValidParametersException("Введеные поля формы введены некоректно либо отсутствуют.");
    }

    public static void areValidRateParameters(String baseCode, String targetCode, String rate) {
        if (isInvalidCode(baseCode) || isInvalidCode(targetCode) || rate == null || rate.isEmpty())
            throw new NoValidParametersException("Введеные поля формы введены некоректно либо отсутствуют.");
    }

    public static void areValidRateParameters(String code, String rate) {
        if (isInvalidCode(code) || rate == null || rate.isEmpty())
            throw new NoValidParametersException("Введеные поля формы введены некоректно либо отсутствуют.");
    }

    private static boolean isInvalidCode(String code) {
    return code == null && code.length() != 3;
    }
}
