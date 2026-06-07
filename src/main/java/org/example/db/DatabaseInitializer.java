package org.example.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    private final ConnectionFactory connectionFactory;

    public DatabaseInitializer(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void initialize() {
        String sql = readSchemaSql();

        try (
                Connection connection = connectionFactory.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(sql);
        } catch (Exception exception) {
            throw new RuntimeException("Не удалось инициализировать бд", exception);
        }
    }

    private String readSchemaSql() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("schema.sql");

        if (inputStream == null) {
            throw new RuntimeException("Файл schema.sql не найден");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        )) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception exception) {
            throw new RuntimeException("Не удалось прочитать schema.sql", exception);
        }
    }
}