package org.example.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.Salary;
import org.example.domain.Vacancy;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonVacancyExporter implements VacancyExporter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getFormat() {
        return "json";
    }

    @Override
    public void export(List<Vacancy> vacancies, Path path) {
        try {
            List<Map<String, Object>> jsonVacancies = vacancies.stream()
                    .map(this::toJsonObject)
                    .toList();

            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(path.toFile(), jsonVacancies);
        } catch (Exception exception) {
            throw new RuntimeException("Не удалось экспортировать вакансии в JSON", exception);
        }
    }

    private Map<String, Object> toJsonObject(Vacancy vacancy) {
        Salary salary = vacancy.getSalary();

        Map<String, Object> json = new LinkedHashMap<>();

        json.put("title", vacancy.getTitle());
        json.put("company", vacancy.getCompany());
        json.put("city", vacancy.getCity());
        json.put("salaryFrom", salary.getFrom());
        json.put("salaryTo", salary.getTo());
        json.put("currency", salary.getCurrency());
        json.put("description", vacancy.getDescription());
        json.put("sourceUrl", vacancy.getSourceUrl());
        json.put("publishedAt", vacancy.getPublishedAt().toString());

        return json;
    }
}