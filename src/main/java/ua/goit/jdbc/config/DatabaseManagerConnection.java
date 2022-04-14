package ua.goit.jdbc.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManagerConnection {
    private HikariDataSource hikariDataSource;

    public DatabaseManagerConnection(PropertiesConfig propertiesConfig) {
        initDataSource(propertiesConfig);
    }

    public Connection getConnection() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void initDataSource(PropertiesConfig propertiesConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:postgresql://%s/%s", propertiesConfig.getProperty("host"),
                propertiesConfig.getProperty("database.name")));
        config.setUsername(propertiesConfig.getProperty("username"));
        config.setPassword(propertiesConfig.getProperty("password"));
        config.setMaximumPoolSize(10);
        config.setIdleTimeout(10_000);
        config.setConnectionTimeout(10_000);
        hikariDataSource = new HikariDataSource(config);
    }
}
