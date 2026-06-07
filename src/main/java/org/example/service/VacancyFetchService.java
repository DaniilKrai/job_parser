package org.example.service;

import org.example.domain.Vacancy;
import org.example.parser.VacancySourceParser;
import org.example.repository.VacancyRepository;
import org.example.repository.FetchHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;

public class VacancyFetchService {
    private final VacancyRepository vacancyRepository;
    private final List<VacancySourceParser> parsers;
    private final FetchHistoryRepository fetchHistoryRepository;

    public VacancyFetchService(
            VacancyRepository vacancyRepository,
            FetchHistoryRepository fetchHistoryRepository,
            List<VacancySourceParser> parsers
    ) {
        this.vacancyRepository = vacancyRepository;
        this.fetchHistoryRepository = fetchHistoryRepository;
        this.parsers = parsers;
    }

    public int fetchAll() {
        int totalAdded = 0;

        for (VacancySourceParser parser : parsers) {
            LocalDateTime startedAt = LocalDateTime.now();
            try {
                List<Vacancy> vacancies = parser.fetchVacancies();
                int addedCount = vacancyRepository.saveAll(vacancies);
                LocalDateTime finishedAt = LocalDateTime.now();

                fetchHistoryRepository.save(
                        parser.getSourceName(),
                        startedAt,
                        finishedAt,
                        vacancies.size(),
                        addedCount,
                        "SUCCESS",
                        "OK"
                );

                totalAdded += addedCount;

                System.out.println("Источник: " + parser.getSourceName());
                System.out.println("Получено вакансий: " + vacancies.size());
                System.out.println("Добавлено новых: " + addedCount);
            } catch (Exception exception) {
                LocalDateTime finishedAt = LocalDateTime.now();

                fetchHistoryRepository.save(
                        parser.getSourceName(),
                        startedAt,
                        finishedAt,
                        0,
                        0,
                        "FAILED",
                        exception.getMessage()
                );

                System.out.println("Ошибка при загрузке источника: " + parser.getSourceName());
                System.out.println(exception.getMessage());
            }
        }

        return totalAdded;
    }
}