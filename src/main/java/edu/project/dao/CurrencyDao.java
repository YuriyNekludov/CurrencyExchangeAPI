package edu.project.dao;

import edu.project.models.Currency;

import java.sql.SQLException;
import java.util.Optional;

public interface CurrencyDao extends Dao<Currency> {
    Optional<Currency> getElementByCode(String code) throws SQLException;
}
