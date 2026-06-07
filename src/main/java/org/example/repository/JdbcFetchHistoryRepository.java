package org.example.repository;

import org.example.db.ConnectionFactory;
import org.example.domain.FetchHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class JdbcFetchHistoryRepository implements FetchHistoryRepository {
    private final ConnectionFactory connectionFactory;

    public JdbcFetchHistoryRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void save(
            String sourceName,
            java.time.LocalDateTime startedAt,
            java.time.LocalDateTime finishedAt,
            int receivedCount,
            int addedCount,
            String status,
            String message
    ) {
        String sql = """
                INSERT INTO fetch_history (
                    source_name,
                    started_at,
                    finished_at,
                    received_count,
                    added_count,
                    status,
                    message
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, sourceName);
            statement.setTimestamp(2, Timestamp.valueOf(startedAt));
            statement.setTimestamp(3, Timestamp.valueOf(finishedAt));
            statement.setInt(4, receivedCount);
            statement.setInt(5, addedCount);
            statement.setString(6, status);
            statement.setString(7, message);

            statement.executeUpdate();
        } catch (Exception exception) {
            throw new RuntimeException("Не удалось сохранить историю загрузки", exception);
        }
    }

    @Override
    public List<FetchHistory> findAll() {
        String sql = """
                SELECT
                    id,
                    source_name,
                    started_at,
                    finished_at,
                    received_count,
                    added_count,
                    status,
                    message
                FROM fetch_history
                ORDER BY id DESC
                """;

        List<FetchHistory> history = new ArrayList<>();

        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                history.add(new FetchHistory(
                        resultSet.getLong("id"),
                        resultSet.getString("source_name"),
                        resultSet.getTimestamp("started_at").toLocalDateTime(),
                        resultSet.getTimestamp("finished_at").toLocalDateTime(),
                        resultSet.getInt("received_count"),
                        resultSet.getInt("added_count"),
                        resultSet.getString("status"),
                        resultSet.getString("message")
                ));
            }
        } catch (Exception exception) {
            throw new RuntimeException("Не удалось получить историю загрузок", exception);
        }

        return history;
    }
}