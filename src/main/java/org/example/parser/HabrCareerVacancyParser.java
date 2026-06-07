package org.example.parser;

import org.example.domain.Salary;
import org.example.domain.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerVacancyParser implements VacancySourceParser {
    private static final String BASE_URL = "https://career.habr.com";
    private static final String VACANCIES_URL = BASE_URL + "/vacancies";

    @Override
    public String getSourceName() {
        return "Habr Career";
    }

    @Override
    public List<Vacancy> fetchVacancies() {
        try {
            Document document = Jsoup.connect(VACANCIES_URL)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Elements vacancyElements = document.select(".vacancy-card");

            List<Vacancy> vacancies = new ArrayList<>();

            for (Element vacancyElement : vacancyElements) {
                Vacancy vacancy = parseVacancy(vacancyElement);

                if (vacancy != null) {
                    vacancies.add(vacancy);
                }
            }

            return vacancies;
        } catch (Exception exception) {
            throw new RuntimeException("Не удалось загрузить вакансии с Habr Career", exception);
        }
    }

    private Vacancy parseVacancy(Element vacancyElement) {
        Element titleElement = findTitleElement(vacancyElement);

        if (titleElement == null) {
            return null;
        }

        String title = titleElement.text().trim();

        if (title.isBlank()) {
            return null;
        }

        String href = titleElement.attr("href");

        if (href.isBlank()) {
            return null;
        }

        String sourceUrl = href.startsWith("http")
                ? href
                : BASE_URL + href;

        String company = extractCompany(vacancyElement);
        String city = extractCity(vacancyElement);
        Salary salary = extractSalary(vacancyElement);
        String description = vacancyElement.text();

        return new Vacancy(
                title,
                company,
                city,
                salary,
                description,
                sourceUrl,
                LocalDate.now()
        );
    }

    private Element findTitleElement(Element vacancyElement) {
        Element titleElement = vacancyElement.selectFirst(".vacancy-card__title a");

        if (titleElement != null && !titleElement.text().isBlank()) {
            return titleElement;
        }

        titleElement = vacancyElement.selectFirst("a[href^=/vacancies/]:not(:has(img))");

        if (titleElement != null && !titleElement.text().isBlank()) {
            return titleElement;
        }

        for (Element link : vacancyElement.select("a[href^=/vacancies/]")) {
            if (!link.text().isBlank()) {
                return link;
            }
        }

        return null;
    }

    private String extractCompany(Element vacancyElement) {
        Element companyElement = vacancyElement.selectFirst(".vacancy-card__company-title");

        if (companyElement != null && !companyElement.text().isBlank()) {
            return companyElement.text().trim();
        }

        companyElement = vacancyElement.selectFirst("a[href^=/companies/]");

        if (companyElement != null && !companyElement.text().isBlank()) {
            return companyElement.text().trim();
        }

        return "Не указана";
    }

    private String extractCity(Element vacancyElement) {
        String text = vacancyElement.text();

        if (text.contains("Москва")) {
            return "Москва";
        }

        if (text.contains("Санкт-Петербург")) {
            return "Санкт-Петербург";
        }

        if (text.contains("Питер")) {
            return "Санкт-Петербург";
        }

        if (text.contains("Удаленно") || text.contains("Удалённо")) {
            return "Удалённо";
        }

        return "Не указан";
    }

    private Salary extractSalary(Element vacancyElement) {
        Element salaryElement = vacancyElement.selectFirst(".basic-salary");

        if (salaryElement == null || salaryElement.text().isBlank()) {
            String fullText = vacancyElement.text();

            if (fullText.contains("₽") || fullText.toLowerCase().contains("руб")) {
                return parseSalaryText(fullText);
            }

            return new Salary(null, null, "RUB");
        }

        return parseSalaryText(salaryElement.text());
    }

    private Salary parseSalaryText(String salaryText) {
        String normalized = salaryText
                .replace("\u00a0", " ")
                .replace("₽", " ")
                .replace("руб.", " ")
                .replace("руб", " ")
                .trim();

        List<Integer> numbers = extractNumbers(normalized);

        if (numbers.isEmpty()) {
            return new Salary(null, null, "RUB");
        }

        String lowerText = normalized.toLowerCase();

        if (lowerText.contains("от")) {
            return new Salary(numbers.get(0), null, "RUB");
        }

        if (lowerText.contains("до")) {
            return new Salary(null, numbers.get(0), "RUB");
        }

        if (numbers.size() >= 2) {
            return new Salary(numbers.get(0), numbers.get(1), "RUB");
        }

        return new Salary(numbers.get(0), null, "RUB");
    }

    private List<Integer> extractNumbers(String text) {
        List<Integer> numbers = new ArrayList<>();

        String[] parts = text.split("[^0-9]+");

        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }

            try {
                numbers.add(Integer.parseInt(part));
            } catch (NumberFormatException ignored) {
            }
        }

        return numbers;
    }
}