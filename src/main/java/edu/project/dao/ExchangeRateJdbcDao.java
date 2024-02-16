package edu.project.dao;

import edu.project.dto.ExchangeRateDto;
import edu.project.models.Currency;
import edu.project.models.ExchangeRate;
import edu.project.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateJdbcDao implements ExchangeRateDao {

    private static final ExchangeRateJdbcDao EXCHANGE_RATE_DAO_INSTANCE = new ExchangeRateJdbcDao();
    private final String sqlQuery = """
                SELECT 
                exchange_rates.id AS e_id,
                base_curr.id AS bc_id,
                base_curr.code AS bc_code,
                base_curr.full_name AS bc_name,
                base_curr.sign AS bc_sign,
                target_curr.id AS tc_id,
                target_curr.code AS tc_code,
                target_curr.full_name AS tc_name,
                target_curr.sign AS tc_sign,
                exchange_rates.rate AS e_rate
                FROM
                exchange_rates
                JOIN
                currencies AS base_curr ON exchange_rates.base_currency_id = base_curr.id
                JOIN
                currencies AS target_curr ON exchange_rates.target_currency_id = target_curr.id
                """;

    private ExchangeRateJdbcDao() {
    }

    public static ExchangeRateJdbcDao getExchangeRateDao() {
        return EXCHANGE_RATE_DAO_INSTANCE;
    }

    @Override
    public ExchangeRate addElement(ExchangeRate exchangeRate) throws SQLException {
        String sql = "INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, exchangeRate.getBaseCurrency().getId());
            statement.setInt(2, exchangeRate.getTargetCurrency().getId());
            statement.setBigDecimal(3, exchangeRate.getRate());
            statement.executeUpdate();
            ResultSet set = statement.getGeneratedKeys();
            exchangeRate.setId(set.getInt(1));
            return exchangeRate;
        }
    }

    @Override
    public List<ExchangeRate> getAllElements() throws SQLException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String sql = sqlQuery + ";";
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                ExchangeRate rate = getExchangeRate(set);
                exchangeRates.add(rate);
            }
        }
        return exchangeRates;
    }

    @Override
    public Optional<ExchangeRate> getElementByCode(String baseCode, String targetCode) throws SQLException {
        String sql = sqlQuery + "WHERE base_curr.code = ? AND target_curr.code = ?;";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, baseCode);
            statement.setString(2, targetCode);
            ResultSet set = statement.executeQuery();
            if (set.next())
                return Optional.of(getExchangeRate(set));
        }
        return Optional.empty();
    }

    @Override
    public List<ExchangeRate> getExchangeRateByCrossCourse(String base, String target) throws SQLException {
        List<ExchangeRate> rateList = new ArrayList<>();
        String sql = sqlQuery + "WHERE base_curr.code = 'USD' AND (target_curr.code = ? OR target_curr.code = ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, base);
            statement.setString(2, target);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                ExchangeRate rate = getExchangeRate(set);
                rateList.add(rate);
            }
        }
        return rateList;
    }

    @Override
    public void updateElement(ExchangeRate rate) throws SQLException {
        String sql = "UPDATE exchange_rates SET rate = ? WHERE exchange_rates.id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, rate.getRate());
            statement.setInt(2, rate.getId());
            statement.executeUpdate();
        }
    }

    private ExchangeRate getExchangeRate(ResultSet set) throws SQLException {
        Currency base = new Currency(
                set.getInt("bc_id"),
                set.getString("bc_code"),
                set.getString("bc_name"),
                set.getString("bc_sign"));
        Currency target = new Currency(
                set.getInt("tc_id"),
                set.getString("tc_code"),
                set.getString("tc_name"),
                set.getString("tc_sign"));
        return new ExchangeRate(
                set.getInt("e_id"),
                base,
                target,
                set.getBigDecimal("e_rate"));
    }
}
