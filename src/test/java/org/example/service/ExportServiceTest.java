package org.example.service;

import org.example.domain.Salary;
import org.example.domain.Vacancy;
import org.example.exporter.VacancyExporter;
import org.example.repository.VacancyRepository;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ExportServiceTest {

    @Test
    void shouldExportVacanciesUsingCorrectExporter() {
        VacancyRepository vacancyRepository = mock(VacancyRepository.class);
        VacancyExporter csvExporter = mock(VacancyExporter.class);

        List<Vacancy> vacancies = List.of(
                new Vacancy(
                        "Java Developer",
                        "Test Company",
                        "Москва",
                        new Salary(150000, 250000, "RUB"),
                        "Java backend development",
                        "https://example.com/java",
                        LocalDate.now()
                )
        );

        when(vacancyRepository.findAll()).thenReturn(vacancies);
        when(csvExporter.getFormat()).thenReturn("csv");

        ExportService exportService = new ExportService(
                vacancyRepository,
                List.of(csvExporter)
        );

        exportService.export("csv", "vacancies.csv");

        verify(vacancyRepository).findAll();
        verify(csvExporter).export(vacancies, Path.of("vacancies.csv"));
    }

    @Test
    void shouldThrowExceptionWhenExporterFormatIsUnknown() {
        VacancyRepository vacancyRepository = mock(VacancyRepository.class);
        VacancyExporter csvExporter = mock(VacancyExporter.class);

        when(csvExporter.getFormat()).thenReturn("csv");

        ExportService exportService = new ExportService(
                vacancyRepository,
                List.of(csvExporter)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> exportService.export("xml", "vacancies.xml")
        );

        verify(vacancyRepository, never()).findAll();
        verify(csvExporter, never()).export(any(), any());
    }
}
