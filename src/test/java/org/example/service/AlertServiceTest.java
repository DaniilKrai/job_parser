package org.example.service;

import org.example.domain.Alert;
import org.example.domain.Salary;
import org.example.domain.SearchCriteria;
import org.example.domain.Vacancy;
import org.example.repository.AlertRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlertServiceTest {

    @Test
    void shouldAddAlert() {
        AlertRepository alertRepository = mock(AlertRepository.class);
        VacancySearchService vacancySearchService = mock(VacancySearchService.class);

        AlertService alertService = new AlertService(
                alertRepository,
                vacancySearchService
        );

        alertService.addAlert("Java", "Москва", 150000);

        ArgumentCaptor<Alert> alertCaptor = ArgumentCaptor.forClass(Alert.class);

        verify(alertRepository).save(alertCaptor.capture());

        Alert savedAlert = alertCaptor.getValue();

        assertEquals("Java", savedAlert.getKeyword());
        assertEquals("Москва", savedAlert.getCity());
        assertEquals(150000, savedAlert.getMinSalary());
        assertNotNull(savedAlert.getCreatedAt());
    }

    @Test
    void shouldCheckAlertsAndReturnMatchingVacancies() {
        AlertRepository alertRepository = mock(AlertRepository.class);
        VacancySearchService vacancySearchService = mock(VacancySearchService.class);

        Alert alert = new Alert(
                1L,
                "Java",
                "Москва",
                150000,
                java.time.LocalDateTime.now()
        );

        Vacancy vacancy = new Vacancy(
                "Java Developer",
                "Test Company",
                "Москва",
                new Salary(150000, 250000, "RUB"),
                "Java backend development",
                "https://example.com/java",
                LocalDate.now()
        );

        when(alertRepository.findAll()).thenReturn(List.of(alert));
        when(vacancySearchService.search(any(SearchCriteria.class))).thenReturn(List.of(vacancy));

        AlertService alertService = new AlertService(
                alertRepository,
                vacancySearchService
        );

        Map<Alert, List<Vacancy>> result = alertService.checkAlerts();

        assertEquals(1, result.size());
        assertTrue(result.containsKey(alert));
        assertEquals(1, result.get(alert).size());
        assertEquals("Java Developer", result.get(alert).get(0).getTitle());

        verify(alertRepository).findAll();
        verify(vacancySearchService).search(any(SearchCriteria.class));
    }
}
