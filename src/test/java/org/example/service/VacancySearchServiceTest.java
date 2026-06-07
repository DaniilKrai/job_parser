package org.example.service;

import org.example.domain.Salary;
import org.example.domain.SearchCriteria;
import org.example.domain.Vacancy;
import org.example.repository.InMemoryVacancyRepository;
import org.example.repository.VacancyRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VacancySearchServiceTest {

    @Test
    void shouldSearchVacanciesByKeyword() {
        VacancyRepository repository = new InMemoryVacancyRepository();
        VacancySearchService service = new VacancySearchService(repository);

        repository.saveAll(List.of(
                new Vacancy(
                        "Java Developer",
                        "Example Company",
                        "Москва",
                        new Salary(150000, 250000, "RUB"),
                        "Разработка backend-сервисов на Java",
                        "https://example.com/java",
                        LocalDate.now()
                ),
                new Vacancy(
                        "QA Engineer",
                        "Demo Tech",
                        "Питер",
                        new Salary(90000, 140000, "RUB"),
                        "Тестирование web-приложений",
                        "https://example.com/qa",
                        LocalDate.now()
                )
        ));

        List<Vacancy> result = service.search(SearchCriteria.byKeyword("java"));

        assertEquals(1, result.size());
        assertEquals("Java Developer", result.get(0).getTitle());
    }

    @Test
    void shouldFilterVacanciesByCity() {
        VacancyRepository repository = new InMemoryVacancyRepository();
        VacancySearchService service = new VacancySearchService(repository);

        repository.saveAll(List.of(
                new Vacancy(
                        "Java Developer",
                        "Example Company",
                        "Москва",
                        new Salary(150000, 250000, "RUB"),
                        "Разработка backend-сервисов на Java",
                        "https://example.com/java",
                        LocalDate.now()
                ),
                new Vacancy(
                        "QA Engineer",
                        "Demo Tech",
                        "Питер",
                        new Salary(90000, 140000, "RUB"),
                        "Тестирование web-приложений",
                        "https://example.com/qa",
                        LocalDate.now()
                )
        ));

        List<Vacancy> result = service.search(SearchCriteria.byCity("Москва"));

        assertEquals(1, result.size());
        assertEquals("Москва", result.get(0).getCity());
    }

    @Test
    void shouldFilterVacanciesByMinSalary() {
        VacancyRepository repository = new InMemoryVacancyRepository();
        VacancySearchService service = new VacancySearchService(repository);

        repository.saveAll(List.of(
                new Vacancy(
                        "Java Developer",
                        "Example Company",
                        "Москва",
                        new Salary(150000, 250000, "RUB"),
                        "Разработка backend-сервисов на Java",
                        "https://example.com/java",
                        LocalDate.now()
                ),
                new Vacancy(
                        "QA Engineer",
                        "Demo Tech",
                        "Питер",
                        new Salary(90000, 140000, "RUB"),
                        "Тестирование web-приложений",
                        "https://example.com/qa",
                        LocalDate.now()
                )
        ));

        List<Vacancy> result = service.search(SearchCriteria.byMinSalary(150000));

        assertEquals(1, result.size());
        assertEquals("Java Developer", result.get(0).getTitle());
    }

    @Test
    void shouldReturnEmptyListWhenNothingFound() {
        VacancyRepository repository = new InMemoryVacancyRepository();
        VacancySearchService service = new VacancySearchService(repository);

        repository.save(new Vacancy(
                "Java Developer",
                "Example Company",
                "Москва",
                new Salary(150000, 250000, "RUB"),
                "Разработка backend-сервисов на Java",
                "https://example.com/java",
                LocalDate.now()
        ));

        List<Vacancy> result = service.search(SearchCriteria.byKeyword("python"));

        assertTrue(result.isEmpty());
    }
}