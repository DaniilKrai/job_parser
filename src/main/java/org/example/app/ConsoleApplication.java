package org.example.app;

import org.example.domain.Salary;
import org.example.domain.Vacancy;
import org.example.repository.VacancyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleApplication {
    private final Scanner scanner = new Scanner(System.in);
    private final VacancyRepository vacancyRepository;

    public ConsoleApplication(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    public void run () {
        System.out.println("Job Parser запущен");
        System.out.println("введите help для списка команд");
        boolean running =true;
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equals("help")) {
                printHelp();
            } else if (input.equals("add-demo")) {
                addDemoVacancies();
            } else if (input.equals("list")) {
                printVacancies();
            } else if (input.equals("exit")) {
                running = false;
            } else if (input.isBlank()) {
                System.out.println("введите команду");
            } else {
                System.out.println("Неизвестная команда, введите help");
            }
        }
        System.out.println("завершено");
    }

    private void printHelp() {
        System.out.println("Доступные команды:");
        System.out.println("help - показать список команд");
        System.out.println("add-demo - добавить демо-вакансии");
        System.out.println("list - показать список вакансий");
        System.out.println("exit - завершить");
    }

    private void addDemoVacancies() {
        List<Vacancy> demoVacancies = List.of(
                new Vacancy(
                        "Java Developer",
                        "Example Company",
                        "Москва",
                        new Salary(150000, 250000, "RUB"),
                        "Разработка backend-сервисов на Java",
                        "https://example.com/vacancies/developer",
                        LocalDate.now()
                ),

                new Vacancy(
                        "QA Engineer",
                        "Demo Tech",
                        "Питер",
                        new Salary(90000, 140000, "RUB"),
                        "Тестирование web-приложений",
                        "https://example.com/vacancies/engineer",
                        LocalDate.now()
                ),

                new Vacancy(
                        "Frontend Developer",
                        "Web Studio",
                        "Удалённо",
                        new Salary(120000, 200000, "RUB"),
                        "Разработка пользовательских интерфейсов",
                        "https://example.com/vacancies/frontend-developer",
                        LocalDate.now()
                )
                );

        vacancyRepository.saveAll(demoVacancies);

        System.out.println("Демо-вакансии добавлены");
    }

    private void printVacancies() {
        List<Vacancy> vacancies = vacancyRepository.findAll();
        if (vacancies.isEmpty()) {
            System.out.println("Список вакансий пуст");
            return;
        }

        System.out.println("Список вакансий:");
        for (Vacancy vacancy : vacancies) {
            System.out.println(vacancy);
        }
    }
}
