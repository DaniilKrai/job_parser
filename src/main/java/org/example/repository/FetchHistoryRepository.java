package org.example.repository;

import org.example.domain.FetchHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface FetchHistoryRepository {
    void save(
            String sourceName,
            LocalDateTime startedAt,
            LocalDateTime finishedAt,
            int receivedCount,
            int addedCount,
            String status,
            String message
    );

    List<FetchHistory> findAll();
}