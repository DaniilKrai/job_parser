package org.example;

import org.example.domain.Salary;
import org.example.domain.Vacancy;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Vacancy javaVacancy = new Vacancy(
                "Java Developer",
                "Example Company",
                "Москва",
                new Salary(150000, 250000, "RUB"),
                "Разработка backend-сервисов на Java",
                "https://example.com/vacancies/developer",
                LocalDate.now()
        );

        Vacancy qaVacancy = new Vacancy(
                "QA Engineer",
                "Demo Tech",
                "Санкт-Петербург",
                new Salary(90000, 140000, "RUB"),
                "Тестирование web-приложений",
                "https://example.com/vacancies/engineer",
                LocalDate.now()
        );

        List<Vacancy> vacancies = List.of(javaVacancy, qaVacancy);

        System.out.println("Список вакансий:");
        for (Vacancy vacancy : vacancies) {
            System.out.println(vacancy);
        }
    }
}