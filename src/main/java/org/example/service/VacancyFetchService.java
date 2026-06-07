package org.example.service;

import org.example.domain.Vacancy;
import org.example.parser.VacancySourceParser;
import org.example.repository.VacancyRepository;

import java.util.List;

public class VacancyFetchService {
    private final VacancyRepository vacancyRepository;
    private final List<VacancySourceParser> parsers;

    public VacancyFetchService(
            VacancyRepository vacancyRepository,
            List<VacancySourceParser> parsers
    ) {
        this.vacancyRepository = vacancyRepository;
        this.parsers = parsers;
    }

    public int fetchAll() {
        int totalAdded = 0;

        for (VacancySourceParser parser : parsers) {
            List<Vacancy> vacancies = parser.fetchVacancies();
            int addedCount = vacancyRepository.saveAll(vacancies);

            totalAdded += addedCount;

            System.out.println("Источник: " + parser.getSourceName());
            System.out.println("Получено вакансий: " + vacancies.size());
            System.out.println("Добавлено новых: " + addedCount);
        }

        return totalAdded;
    }
}