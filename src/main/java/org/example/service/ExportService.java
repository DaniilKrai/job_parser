package org.example.service;

import org.example.domain.Vacancy;
import org.example.exporter.VacancyExporter;
import org.example.repository.VacancyRepository;

import java.nio.file.Path;
import java.util.List;

public class ExportService {
    private final VacancyRepository vacancyRepository;
    private final List<VacancyExporter> exporters;

    public ExportService(
            VacancyRepository vacancyRepository,
            List<VacancyExporter> exporters
    ) {
        this.vacancyRepository = vacancyRepository;
        this.exporters = exporters;
    }

    public void export(String format, String fileName) {
        VacancyExporter exporter = findExporter(format);

        List<Vacancy> vacancies = vacancyRepository.findAll();

        exporter.export(vacancies, Path.of(fileName));
    }

    private VacancyExporter findExporter(String format) {
        return exporters.stream()
                .filter(exporter -> exporter.getFormat().equalsIgnoreCase(format))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный формат экспорта: " + format));
    }
}