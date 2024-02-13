package edu.project.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionManager {
    private static HikariDataSource connectionPool;

    private ConnectionManager() {
    }

    public static Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    static void closeConnectionPool() {
        connectionPool.close();
    }

    static void initPool() {
        try {
            String path = Paths.get(ConnectionManager.class.getClassLoader()
                    .getResource("currencies.db").toURI()).toAbsolutePath().toString();
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("org.sqlite.JDBC");
            config.setJdbcUrl("jdbc:sqlite:" + path);
            config.setMaximumPoolSize(10);
            connectionPool = new HikariDataSource(config);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
