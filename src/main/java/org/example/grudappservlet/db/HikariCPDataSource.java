package org.example.grudappservlet.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HikariCPDataSource implements ConnectionManager, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(HikariCPDataSource.class);
    private final transient HikariDataSource dataSource;

    public HikariCPDataSource() {
        HikariConfig config = initializeConfig();
        assert config != null;
        this.dataSource = new HikariDataSource(config);
    }

    protected HikariCPDataSource(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    private HikariConfig initializeConfig() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new FileNotFoundException("db.properties not found");
            }
            properties.load(input);
            return new HikariConfig(properties);
        } catch (IOException e) {
            LOGGER.error("Failed to initialize HikariConfig", e);
            return null;
        }
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Failed to get connection", e);
            return null;
        }
    }
}
