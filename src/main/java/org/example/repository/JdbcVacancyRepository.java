package org.example.repository;

import org.example.db.ConnectionFactory;
import org.example.domain.Salary;
import org.example.domain.Vacancy;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JdbcVacancyRepository implements VacancyRepository {
    private final ConnectionFactory connectionFactory;

    public JdbcVacancyRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public boolean save(Vacancy vacancy) {
        if (existsBySourceUrl(vacancy.getSourceUrl())) {
            return false;
        }

        String sql = """
                INSERT INTO vacancies (
                    title,
                    company,
                    city,
                    salary_from,
                    salary_to,
                    currency,
                    description,
                    source_url,
                    published_at
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, vacancy.getTitle());
            statement.setString(2, vacancy.getCompany());
            statement.setString(3, vacancy.getCity());

            Salary salary = vacancy.getSalary();

            if (salary.getFrom() == null) {
                statement.setNull(4, java.sql.Types.INTEGER);
            } else {
                statement.setInt(4, salary.getFrom());
            }

            if (salary.getTo() == null) {
                statement.setNull(5, java.sql.Types.INTEGER);
            } else {
                statement.setInt(5, salary.getTo());
            }

            statement.setString(6, salary.getCurrency());
            statement.setString(7, vacancy.getDescription());
            statement.setString(8, vacancy.getSourceUrl());
            statement.setDate(9, Date.valueOf(vacancy.getPublishedAt()));

            statement.executeUpdate();
            return true;
        } catch (Exception exception) {
            throw new RuntimeException("Не удалось сохранить вакансию", exception);
        }
    }

    @Override
    public int saveAll(List<Vacancy> vacancies) {
        int addedCount = 0;
        for (Vacancy vacancy : vacancies) {
            if (save(vacancy)) {
                ++addedCount;
            }
        }
        return addedCount;
    }

    @Override
    public List<Vacancy> findAll() {
        String sql = """
                SELECT
                    title,
                    company,
                    city,
                    salary_from,
                    salary_to,
                    currency,
                    description,
                    source_url,
                    published_at
                FROM vacancies
                ORDER BY published_at DESC, id DESC
                """;

        List<Vacancy> vacancies = new ArrayList<>();

        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                vacancies.add(mapRowToVacancy(resultSet));
            }
        } catch (Exception exception) {
            throw new RuntimeException("Не удалось получить список вакансий", exception);
        }

        return vacancies;
    }

    private boolean existsBySourceUrl(String sourceUrl) {
        String sql = "SELECT COUNT(*) FROM vacancies WHERE source_url = ?";

        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, sourceUrl);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        } catch (Exception exception) {
            throw new RuntimeException("Не удалось проверить существование вакансии", exception);
        }
    }

    private Vacancy mapRowToVacancy(ResultSet resultSet) throws Exception {
        Integer salaryFrom = getIntegerOrNull(resultSet, "salary_from");
        Integer salaryTo = getIntegerOrNull(resultSet, "salary_to");

        Salary salary = new Salary(
                salaryFrom,
                salaryTo,
                resultSet.getString("currency")
        );

        return new Vacancy(
                resultSet.getString("title"),
                resultSet.getString("company"),
                resultSet.getString("city"),
                salary,
                resultSet.getString("description"),
                resultSet.getString("source_url"),
                resultSet.getDate("published_at").toLocalDate()
        );
    }

    private Integer getIntegerOrNull(ResultSet resultSet, String columnName) throws Exception {
        int value = resultSet.getInt(columnName);

        if (resultSet.wasNull()) {
            return null;
        }

        return value;
    }
}