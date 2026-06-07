package org.example.parser;

import org.example.domain.Vacancy;

import java.util.List;

public interface VacancySourceParser {
    String getSourceName();

    List<Vacancy> fetchVacancies();
}