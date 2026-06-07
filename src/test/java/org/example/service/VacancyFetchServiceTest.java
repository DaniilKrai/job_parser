package org.example.service;

import org.example.domain.Salary;
import org.example.domain.Vacancy;
import org.example.parser.VacancySourceParser;
import org.example.repository.FetchHistoryRepository;
import org.example.repository.VacancyRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class VacancyFetchServiceTest {

    @Test
    void shouldFetchVacanciesAndSaveHistoryWhenParserWorks() {
        VacancyRepository vacancyRepository = mock(VacancyRepository.class);
        FetchHistoryRepository fetchHistoryRepository = mock(FetchHistoryRepository.class);
        VacancySourceParser parser = mock(VacancySourceParser.class);

        List<Vacancy> vacancies = List.of(
                new Vacancy(
                        "Java Developer",
                        "Test Company",
                        "Москва",
                        new Salary(150000, 250000, "RUB"),
                        "Java backend development",
                        "https://example.com/java",
                        LocalDate.now()
                ),
                new Vacancy(
                        "QA Engineer",
                        "Test Company",
                        "Москва",
                        new Salary(100000, 150000, "RUB"),
                        "Testing web applications",
                        "https://example.com/qa",
                        LocalDate.now()
                )
        );

        when(parser.getSourceName()).thenReturn("test-source");
        when(parser.fetchVacancies()).thenReturn(vacancies);
        when(vacancyRepository.saveAll(vacancies)).thenReturn(2);

        VacancyFetchService service = new VacancyFetchService(
                vacancyRepository,
                fetchHistoryRepository,
                List.of(parser)
        );

        int addedCount = service.fetchAll();

        assertEquals(2, addedCount);

        verify(parser).fetchVacancies();
        verify(vacancyRepository).saveAll(vacancies);

        verify(fetchHistoryRepository).save(
                eq("test-source"),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(2),
                eq(2),
                eq("SUCCESS"),
                eq("OK")
        );
    }

    @Test
    void shouldSaveFailedHistoryWhenParserThrowsException() {
        VacancyRepository vacancyRepository = mock(VacancyRepository.class);
        FetchHistoryRepository fetchHistoryRepository = mock(FetchHistoryRepository.class);
        VacancySourceParser parser = mock(VacancySourceParser.class);

        when(parser.getSourceName()).thenReturn("broken-source");
        when(parser.fetchVacancies()).thenThrow(new RuntimeException("Parser error"));

        VacancyFetchService service = new VacancyFetchService(
                vacancyRepository,
                fetchHistoryRepository,
                List.of(parser)
        );

        int addedCount = service.fetchAll();

        assertEquals(0, addedCount);

        verify(vacancyRepository, never()).saveAll(any());

        verify(fetchHistoryRepository).save(
                eq("broken-source"),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(0),
                eq(0),
                eq("FAILED"),
                eq("Parser error")
        );
    }
}
