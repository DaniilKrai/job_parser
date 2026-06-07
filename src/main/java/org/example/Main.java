package org.example;

import org.example.app.ConsoleApplication;
import org.example.repository.InMemoryVacancyRepository;
import org.example.repository.VacancyRepository;

public class Main {
    public static void main(String[] args) {
        VacancyRepository vacancyRepository = new InMemoryVacancyRepository();

        ConsoleApplication application = new ConsoleApplication(vacancyRepository);
        application.run();
    }
}