package org.example.repository;

import org.example.domain.Vacancy;

import java.util.ArrayList;
import java.util.List;

public class InMemoryVacancyRepository implements VacancyRepository {
    private final List<Vacancy> vacancies = new ArrayList<>();

    @Override
    public void save(Vacancy vacancy) {
        vacancies.add(vacancy);
    }

    @Override
    public void saveAll(List<Vacancy> vacancies) {
        this.vacancies.addAll(vacancies);
    }

    @Override
    public List<Vacancy> findAll() {
        return new ArrayList<>(vacancies);
    }
}
