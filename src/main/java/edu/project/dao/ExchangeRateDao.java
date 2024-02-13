package edu.project.dao;

import edu.project.dto.ExchangeRateDto;
import edu.project.models.ExchangeRate;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateDao extends Dao<ExchangeRate> {
    List<ExchangeRate> getExchangeRateByCrossCourse(String base, String target) throws SQLException;

    Optional<ExchangeRate> getElementByCode(String baseCode, String targetCode) throws SQLException;

    void updateElement(ExchangeRateDto rate) throws SQLException;
}
