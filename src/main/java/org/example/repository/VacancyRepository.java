package org.example.repository;
import org.example.domain.Vacancy;
import java.util.List;

public interface VacancyRepository {
    boolean save(Vacancy vacancy);

    int saveAll(List<Vacancy> vacancies);

    List<Vacancy> findAll();
}
