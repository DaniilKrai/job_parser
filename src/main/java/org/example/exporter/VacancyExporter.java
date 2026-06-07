package org.example.exporter;

import org.example.domain.Vacancy;

import java.nio.file.Path;
import java.util.List;

public interface VacancyExporter {
    String getFormat();

    void export(List<Vacancy> vacancies, Path path);
}