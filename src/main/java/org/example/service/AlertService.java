package org.example.service;

import org.example.domain.Alert;
import org.example.domain.SearchCriteria;
import org.example.domain.Vacancy;
import org.example.repository.AlertRepository;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AlertService {
    private final AlertRepository alertRepository;
    private final VacancySearchService vacancySearchService;

    public AlertService(
            AlertRepository alertRepository,
            VacancySearchService vacancySearchService
    ) {
        this.alertRepository = alertRepository;
        this.vacancySearchService = vacancySearchService;
    }

    public void addAlert(String keyword, String city, Integer minSalary) {
        Alert alert = new Alert(
                null,
                keyword,
                city,
                minSalary,
                LocalDateTime.now()
        );

        alertRepository.save(alert);
    }

    public List<Alert> findAll() {
        return alertRepository.findAll();
    }

    public Map<Alert, List<Vacancy>> checkAlerts() {
        Map<Alert, List<Vacancy>> result = new LinkedHashMap<>();

        for (Alert alert : alertRepository.findAll()) {
            SearchCriteria criteria = new SearchCriteria(
                    alert.getKeyword(),
                    alert.getCity(),
                    null,
                    alert.getMinSalary()
            );

            List<Vacancy> vacancies = vacancySearchService.search(criteria);
            result.put(alert, vacancies);
        }

        return result;
    }
}
