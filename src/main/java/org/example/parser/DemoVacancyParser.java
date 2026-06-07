package org.example.parser;

import org.example.domain.Salary;
import org.example.domain.Vacancy;

import java.time.LocalDate;
import java.util.List;

public class DemoVacancyParser implements VacancySourceParser {

    @Override
    public String getSourceName() {
        return "demo";
    }

    @Override
    public List<Vacancy> fetchVacancies() {
        return List.of(
                new Vacancy(
                        "Backend Java Developer",
                        "Parser Demo Company",
                        "мск",
                        new Salary(180000, 280000, "RUB"),
                        "Разработка backend-сервисов, работа с Java, SQL и REST API",
                        "https://demo.example.com/vacancies/backend-java-developer",
                        LocalDate.now()
                ),
                new Vacancy(
                        "Middle QA Automation Engineer",
                        "Parser Demo Company",
                        "Питер",
                        new Salary(130000, 210000, "RUB"),
                        "Автоматизация тестирования web-приложений",
                        "https://demo.example.com/vacancies/qa-automation-engineer",
                        LocalDate.now()
                ),
                new Vacancy(
                        "Junior Java Developer",
                        "Internship Lab",
                        "Удалённо",
                        new Salary(70000, 120000, "RUB"),
                        "Участие в разработке Java-приложений под руководством наставника",
                        "https://demo.example.com/vacancies/junior-java-developer",
                        LocalDate.now()
                )
        );
    }
}