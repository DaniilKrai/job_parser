package org.example.repository;

import org.example.domain.Vacancy;

import java.util.ArrayList;
import java.util.List;

public class InMemoryVacancyRepository implements VacancyRepository {
    private final List<Vacancy> vacancies = new ArrayList<>();

    @Override
    public boolean save(Vacancy vacancy) {
        if (existsBySourceUrl(vacancy.getSourceUrl())) {
            return false;
        }

        vacancies.add(vacancy);
        return true;
    }

    @Override
    public int saveAll(List<Vacancy> vacancies) {
        int addedCount= 0;
        for (Vacancy vacancy : vacancies) {
            if (save(vacancy)) {
                ++addedCount;
            }
        }
        return addedCount;
    }

    @Override
    public List<Vacancy> findAll() {
        return new ArrayList<>(vacancies);
    }

    private boolean existsBySourceUrl(String sourceUrl) {
        return vacancies.stream()
                .anyMatch(vacancy -> vacancy.getSourceUrl().equals(sourceUrl));
    }
}
