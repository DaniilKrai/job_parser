package org.example;

import org.example.app.ConsoleApplication;
import org.example.repository.InMemoryVacancyRepository;
import org.example.repository.VacancyRepository;
import org.example.service.VacancySearchService;

public class Main {
    public static void main(String[] args) {
        VacancyRepository vacancyRepository = new InMemoryVacancyRepository();
        VacancySearchService vacancySearchService = new VacancySearchService(vacancyRepository);

        ConsoleApplication application = new ConsoleApplication(
                vacancyRepository,
                vacancySearchService
        );

        application.run();
    }
}