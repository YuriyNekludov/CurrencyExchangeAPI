package edu.project.services;

import edu.project.dto.CurrencyDto;

public interface CurrencyService extends Service<CurrencyDto>{

    CurrencyDto addElement(String code, String name, String sign);
}
