package edu.project.dao;

import edu.project.utils.ConnectionManager;
import edu.project.models.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyJdbcDao implements CurrencyDao {

    private static final CurrencyJdbcDao CURRENCY_DAO_INSTANCE = new CurrencyJdbcDao();
    private String sqlQuery;

    private CurrencyJdbcDao() {
    }

    public static CurrencyJdbcDao getCurrencyDao() {
        return CURRENCY_DAO_INSTANCE;
    }

    @Override
    public Currency addElement(Currency currency) throws SQLException {
        sqlQuery = "INSERT INTO currencies (code, full_name, sign) VALUES (?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.executeUpdate();
            ResultSet set = statement.getGeneratedKeys();
            currency.setId(set.getInt(1));
            return currency;
        }
    }

    @Override
    public List<Currency> getAllElements() throws SQLException {
        List<Currency> currencies = new ArrayList<>();
        sqlQuery = "SELECT * FROM currencies";
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(sqlQuery);
            while (set.next()) {
                Currency currency = getCurrency(set);
                currencies.add(currency);
            }
        }
        return currencies;
    }

    @Override
    public Optional<Currency> getElementByCode(String code) throws SQLException {
        sqlQuery = "SELECT * FROM currencies WHERE code = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, code);
            ResultSet set = statement.executeQuery();
            if (set.next())
                return Optional.of(getCurrency(set));
        }
        return Optional.empty();
    }

    private Currency getCurrency(ResultSet set) throws SQLException {
        return new Currency(
                set.getInt(1),
                set.getString(2),
                set.getString(3),
                set.getString(4));
    }
}
