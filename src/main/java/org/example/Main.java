package org.example;

import org.example.app.ConsoleApplication;
import org.example.db.ConnectionFactory;
import org.example.db.DatabaseInitializer;
import org.example.repository.JdbcVacancyRepository;
import org.example.repository.VacancyRepository;
import org.example.service.VacancySearchService;

public class Main {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        DatabaseInitializer databaseInitializer = new DatabaseInitializer(connectionFactory);
        databaseInitializer.initialize();

        VacancyRepository vacancyRepository = new JdbcVacancyRepository(connectionFactory);
        VacancySearchService vacancySearchService = new VacancySearchService(vacancyRepository);

        ConsoleApplication application = new ConsoleApplication(
                vacancyRepository,
                vacancySearchService
        );

        application.run();
    }
}