package org.example.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String DATABASE_DIRECTORY = "data";
    private static final String DATABASE_URL = "jdbc:h2:file:./data/job_parser";
    private static final String USER = "Daniil";
    private static final String PASSWORD = "";

    public Connection getConnection() {
        try {
            Files.createDirectories(Path.of(DATABASE_DIRECTORY));
            return DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        } catch (IOException exception) {
            throw new RuntimeException("Не удалось создать папку для бд", exception);
        } catch (SQLException exception) {
            throw new RuntimeException("Не удалось подключиться к бд", exception);
        }
    }
}