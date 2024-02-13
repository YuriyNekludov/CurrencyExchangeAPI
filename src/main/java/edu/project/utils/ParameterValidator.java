package edu.project.utils;

import edu.project.exceptions.NoValidParametersException;

public final class ParameterValidator {

    private ParameterValidator() {
    }

    public static void areValidCurrencyParameters(String code, String name, String sign) {
        if (name == null || sign == null || name.isEmpty() || sign.isEmpty() || !checkValidCode(code))
            throw new NoValidParametersException("Введеные поля формы введены некоректно либо отсутствуют.");
    }

    public static void isValidCode(String code) {
        if (!checkValidCode(code))
            throw new NoValidParametersException("Введеные поля формы введены некоректно либо отсутствуют.");
    }

    public static void areValidRateParameters(String baseCode, String targetCode, String rate) {
        if (!checkValidCode(baseCode) || !checkValidCode(targetCode) || rate == null || rate.isEmpty())
            throw new NoValidParametersException("Введеные поля формы введены некоректно либо отсутствуют.");
    }

    public static void areValidRateParameters(String code, String rate) {
        if (!checkValidCode(code) || rate == null || rate.isEmpty())
            throw new NoValidParametersException("Введеные поля формы введены некоректно либо отсутствуют.");
    }

    private static boolean checkValidCode(String code) {
        return code != null && !code.isEmpty() && (code.length() == 3 || code.length() == 6);
    }
}
