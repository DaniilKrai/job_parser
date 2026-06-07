package org.example;

import org.example.app.ConsoleApplication;
import org.example.db.ConnectionFactory;
import org.example.db.DatabaseInitializer;
import org.example.parser.DemoVacancyParser;
import org.example.parser.HabrCareerVacancyParser;
import org.example.parser.VacancySourceParser;
import org.example.repository.FetchHistoryRepository;
import org.example.repository.JdbcFetchHistoryRepository;
import org.example.repository.JdbcVacancyRepository;
import org.example.repository.VacancyRepository;
import org.example.service.VacancyFetchService;
import org.example.service.VacancySearchService;
import org.example.service.VacancyStatisticsService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        DatabaseInitializer databaseInitializer = new DatabaseInitializer(connectionFactory);
        databaseInitializer.initialize();

        VacancyRepository vacancyRepository = new JdbcVacancyRepository(connectionFactory);
        FetchHistoryRepository fetchHistoryRepository = new JdbcFetchHistoryRepository(connectionFactory);

        VacancySearchService vacancySearchService = new VacancySearchService(vacancyRepository);
        VacancyStatisticsService vacancyStatisticsService = new VacancyStatisticsService(vacancyRepository);

        List<VacancySourceParser> parsers = List.of(
                new DemoVacancyParser(),
                new HabrCareerVacancyParser()
        );

        VacancyFetchService vacancyFetchService = new VacancyFetchService(
                vacancyRepository,
                fetchHistoryRepository,
                parsers
        );

        ConsoleApplication application = new ConsoleApplication(
                vacancyRepository,
                vacancySearchService,
                vacancyFetchService,
                fetchHistoryRepository,
                vacancyStatisticsService
        );

        application.run();
    }
}