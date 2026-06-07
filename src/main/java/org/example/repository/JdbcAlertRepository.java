package org.example.repository;

import org.example.db.ConnectionFactory;
import org.example.domain.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class JdbcAlertRepository implements AlertRepository {
    private final ConnectionFactory connectionFactory;

    public JdbcAlertRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void save(Alert alert) {
        String sql = """
                INSERT INTO alerts (
                    keyword,
                    city,
                    min_salary,
                    created_at
                ) VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, alert.getKeyword());
            statement.setString(2, alert.getCity());

            if (alert.getMinSalary() == null) {
                statement.setNull(3, java.sql.Types.INTEGER);
            } else {
                statement.setInt(3, alert.getMinSalary());
            }

            statement.setTimestamp(4, Timestamp.valueOf(alert.getCreatedAt()));
            statement.executeUpdate();
        } catch (Exception exception) {
            throw new RuntimeException("Не удалось сохранить уведомление", exception);
        }
    }

    @Override
    public List<Alert> findAll() {
        String sql = """
                SELECT
                    id,
                    keyword,
                    city,
                    min_salary,
                    created_at
                FROM alerts
                ORDER BY id DESC
                """;

        List<Alert> alerts = new ArrayList<>();

        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                alerts.add(new Alert(
                        resultSet.getLong("id"),
                        resultSet.getString("keyword"),
                        resultSet.getString("city"),
                        getIntegerOrNull(resultSet, "min_salary"),
                        resultSet.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (Exception exception) {
            throw new RuntimeException("Не удалось получить список уведомлений", exception);
        }

        return alerts;
    }

    private Integer getIntegerOrNull(ResultSet resultSet, String columnName) throws Exception {
        int value = resultSet.getInt(columnName);

        if (resultSet.wasNull()) {
            return null;
        }

        return value;
    }
}
