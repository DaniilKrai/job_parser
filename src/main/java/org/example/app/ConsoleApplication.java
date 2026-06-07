package org.example.app;

import org.example.domain.FetchHistory;
import org.example.domain.Salary;
import org.example.domain.SearchCriteria;
import org.example.domain.Vacancy;
import org.example.repository.FetchHistoryRepository;
import org.example.repository.VacancyRepository;
import org.example.service.ExportService;
import org.example.service.VacancyFetchService;
import org.example.service.VacancySearchService;
import org.example.service.VacancyStatisticsService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApplication {
    private final Scanner scanner = new Scanner(System.in);
    private final VacancyRepository vacancyRepository;
    private final VacancySearchService vacancySearchService;
    private final VacancyFetchService vacancyFetchService;
    private final FetchHistoryRepository fetchHistoryRepository;
    private final VacancyStatisticsService vacancyStatisticsService;
    private final ExportService exportService;

    public ConsoleApplication(
            VacancyRepository vacancyRepository,
            VacancySearchService vacancySearchService,
            VacancyFetchService vacancyFetchService,
            FetchHistoryRepository fetchHistoryRepository,
            VacancyStatisticsService vacancyStatisticsService,
            ExportService exportService
    ) {
        this.vacancyRepository = vacancyRepository;
        this.vacancySearchService = vacancySearchService;
        this.vacancyFetchService = vacancyFetchService;
        this.fetchHistoryRepository = fetchHistoryRepository;
        this.vacancyStatisticsService = vacancyStatisticsService;
        this.exportService = exportService;
    }

    public void run() {
        System.out.println("Job Parser запущен");
        System.out.println("Введите help для списка команд");

        boolean running = true;

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equals("help")) {
                printHelp();
            } else if (input.equals("add-demo")) {
                addDemoVacancies();
            } else if (input.equals("fetch")) {
                fetchVacancies();
            } else if (input.equals("history")) {
                printFetchHistory();
            } else if (input.startsWith("stats")) {
                printStats(input);
            } else if (input.startsWith("export ")) {
                exportVacancies(input);
            } else if (input.equals("list")) {
                printVacancies(vacancyRepository.findAll());
            } else if (input.startsWith("search ")) {
                searchByKeyword(input);
            } else if (input.startsWith("filter ")) {
                filterVacancies(input);
            } else if (input.equals("exit")) {
                running = false;
            } else if (input.isBlank()) {
                System.out.println("Введите команду");
            } else {
                System.out.println("Неизвестная команда, введите help");
            }
        }

        System.out.println("Завершено");
    }

    private void printHelp() {
        System.out.println("Доступные команды:");
        System.out.println("help - показать список команд");
        System.out.println("add-demo - добавить демо-вакансии");
        System.out.println("fetch - загрузить вакансии из источников");
        System.out.println("history - показать историю загрузок");
        System.out.println("list - показать список вакансий");
        System.out.println("search <слово> - найти вакансии по слову");
        System.out.println("filter city <город> - фильтр по городу");
        System.out.println("filter company <компания> - фильтр по компании");
        System.out.println("filter salary <сумма> - фильтр по минимальной зарплате");
        System.out.println("stats cities - статистика по городам");
        System.out.println("stats companies - статистика по компаниям");
        System.out.println("stats salary - статистика по зарплатам");
        System.out.println("export csv <file> - экспортировать вакансии в CSV");
        System.out.println("export json <file> - экспортировать вакансии в JSON");
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
                ),

                new Vacancy(
                        "Java Intern",
                        "Junior Lab",
                        "Москва",
                        new Salary(null, 80000, "RUB"),
                        "Стажировка по Java и SQL",
                        "https://example.com/vacancies/java-intern",
                        LocalDate.now()
                )
        );

        int addedCount = vacancyRepository.saveAll(demoVacancies);

        System.out.println("Добавлено новых вакансий: " + addedCount);
    }

    private void fetchVacancies() {
        int totalAdded = vacancyFetchService.fetchAll();

        System.out.println("Всего новых вакансий добавлено: " + totalAdded);
    }

    private void printFetchHistory() {
        List<FetchHistory> history = fetchHistoryRepository.findAll();

        if (history.isEmpty()) {
            System.out.println("История загрузок пустая");
            return;
        }

        System.out.println("История загрузок:");

        for (FetchHistory item : history) {
            System.out.println(item);
        }
    }

    private void searchByKeyword(String input) {
        String keyword = input.substring("search ".length()).trim();

        if (keyword.isBlank()) {
            System.out.println("Укажите слово для поиска");
            return;
        }

        List<Vacancy> result = vacancySearchService.search(SearchCriteria.byKeyword(keyword));
        printVacancies(result);
    }

    private void filterVacancies(String input) {
        String[] parts = input.split("\\s+", 3);

        if (parts.length < 3) {
            System.out.println("Неверный формат команды");
            System.out.println("Примеры:");
            System.out.println("filter city Москва");
            System.out.println("filter company Example Company");
            System.out.println("filter salary 150000");
            return;
        }

        String field = parts[1];
        String value = parts[2];

        if (field.equals("city")) {
            List<Vacancy> result = vacancySearchService.search(SearchCriteria.byCity(value));
            printVacancies(result);
        } else if (field.equals("company")) {
            List<Vacancy> result = vacancySearchService.search(SearchCriteria.byCompany(value));
            printVacancies(result);
        } else if (field.equals("salary")) {
            filterBySalary(value);
        } else {
            System.out.println("Неизвестное поле фильтра: " + field);
            System.out.println("Доступные поля: city, company, salary");
        }
    }

    private void filterBySalary(String value) {
        try {
            int minSalary = Integer.parseInt(value);

            List<Vacancy> result = vacancySearchService.search(SearchCriteria.byMinSalary(minSalary));
            printVacancies(result);
        } catch (NumberFormatException exception) {
            System.out.println("Зарплата должна быть числом");
        }
    }

    private void printStats(String input) {
        if (input.equals("stats cities")) {
            printMapStatistics("Количество вакансий по городам:", vacancyStatisticsService.countByCity());
        } else if (input.equals("stats companies")) {
            printMapStatistics("Количество вакансий по компаниям:", vacancyStatisticsService.countByCompany());
        } else if (input.equals("stats salary")) {
            printSalaryStatistics();
        } else {
            System.out.println("Неизвестная команда статистики");
            System.out.println("Доступные команды:");
            System.out.println("stats cities");
            System.out.println("stats companies");
            System.out.println("stats salary");
        }
    }

    private void printMapStatistics(String title, Map<String, Long> statistics) {
        if (statistics.isEmpty()) {
            System.out.println("Нет данных для статистики");
            return;
        }

        System.out.println(title);

        Map<String, Long> sortedStatistics = vacancyStatisticsService.sortByValueDesc(statistics);

        for (Map.Entry<String, Long> entry : sortedStatistics.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private void printSalaryStatistics() {
        double averageSalary = vacancyStatisticsService.averageSalary();
        List<Vacancy> topSalaryVacancies = vacancyStatisticsService.topBySalary(5);

        if (averageSalary == 0 && topSalaryVacancies.isEmpty()) {
            System.out.println("Нет данных по зарплатам");
            return;
        }

        System.out.println("Средняя зарплата: " + Math.round(averageSalary) + " RUB");
        System.out.println("Топ вакансий по зарплате:");

        printVacancies(topSalaryVacancies);
    }

    private void exportVacancies(String input) {
        String[] parts = input.split("\\s+", 3);

        if (parts.length < 3) {
            System.out.println("Неверный формат команды");
            System.out.println("Пример: export csv vacancies.csv");
            return;
        }

        String format = parts[1];
        String fileName = parts[2];

        try {
            exportService.export(format, fileName);
            System.out.println("Экспорт завершён: " + fileName);
        } catch (Exception exception) {
            System.out.println("Ошибка экспорта: " + exception.getMessage());
        }
    }

    private void printVacancies(List<Vacancy> vacancies) {
        if (vacancies.isEmpty()) {
            System.out.println("Вакансии не найдены");
            return;
        }

        System.out.println("Список вакансий:");

        for (int i = 0; i < vacancies.size(); i++) {
            Vacancy vacancy = vacancies.get(i);
            System.out.println((i + 1) + ". " + vacancy);
        }
    }
}