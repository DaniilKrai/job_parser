package org.example.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoFetchService {
    private final VacancyFetchService vacancyFetchService;
    private ScheduledExecutorService executorService;

    public AutoFetchService(VacancyFetchService vacancyFetchService) {
        this.vacancyFetchService = vacancyFetchService;
    }

    public void start(long intervalSeconds) {
        if (intervalSeconds <= 0) {
            throw new IllegalArgumentException("Интервал должен быть больше 0");
        }

        if (isRunning()) {
            throw new IllegalStateException("Автообновление уже запущено");
        }

        executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.scheduleAtFixedRate(
                this::safeFetch,
                0,
                intervalSeconds,
                TimeUnit.SECONDS
        );
    }

    public void stop() {
        if (!isRunning()) {
            return;
        }

        executorService.shutdownNow();
        executorService = null;
    }

    public boolean isRunning() {
        return executorService != null && !executorService.isShutdown();
    }

    private void safeFetch() {
        try {
            System.out.println();
            System.out.println("[auto-fetch] Запуск автоматического обновления");

            int addedCount = vacancyFetchService.fetchAll();

            System.out.println("[auto-fetch] Добавлено новых вакансий: " + addedCount);
            System.out.print("> ");
        } catch (Exception exception) {
            System.out.println("[auto-fetch] Ошибка: " + exception.getMessage());
            System.out.print("> ");
        }
    }
}
