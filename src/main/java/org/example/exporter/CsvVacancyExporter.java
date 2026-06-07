package org.example.exporter;

import org.example.domain.Salary;
import org.example.domain.Vacancy;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvVacancyExporter implements VacancyExporter {

    @Override
    public String getFormat() {
        return "csv";
    }

    @Override
    public void export(List<Vacancy> vacancies, Path path) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write("title,company,city,salary_from,salary_to,currency,description,source_url,published_at");
            writer.newLine();

            for (Vacancy vacancy : vacancies) {
                writer.write(toCsvLine(vacancy));
                writer.newLine();
            }
        } catch (Exception exception) {
            throw new RuntimeException("Не удалось экспортировать вакансии в CSV", exception);
        }
    }

    private String toCsvLine(Vacancy vacancy) {
        Salary salary = vacancy.getSalary();

        return String.join(",",
                escape(vacancy.getTitle()),
                escape(vacancy.getCompany()),
                escape(vacancy.getCity()),
                escape(salary.getFrom()),
                escape(salary.getTo()),
                escape(salary.getCurrency()),
                escape(vacancy.getDescription()),
                escape(vacancy.getSourceUrl()),
                escape(vacancy.getPublishedAt())
        );
    }

    private String escape(Object value) {
        if (value == null) {
            return "";
        }

        String text = value.toString();

        boolean shouldBeQuoted = text.contains(",")
                || text.contains("\"")
                || text.contains("\n")
                || text.contains("\r");

        text = text.replace("\"", "\"\"");

        if (shouldBeQuoted) {
            return "\"" + text + "\"";
        }

        return text;
    }
}