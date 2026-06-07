package org.example.repository;
import org.example.domain.Vacancy;
import java.util.List;

public interface VacancyRepository {
    void save(Vacancy vacancy);

    void saveAll(List<Vacancy> vacancies);

    List<Vacancy> findAll();
}
