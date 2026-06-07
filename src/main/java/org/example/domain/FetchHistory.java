package org.example.domain;

import java.time.LocalDateTime;

public class FetchHistory {
    private final Long id;
    private final String sourceName;
    private final LocalDateTime startedAt;
    private final LocalDateTime finishedAt;
    private final int receivedCount;
    private final int addedCount;
    private final String status;
    private final String message;

    public FetchHistory(
            Long id,
            String sourceName,
            LocalDateTime startedAt,
            LocalDateTime finishedAt,
            int receivedCount,
            int addedCount,
            String status,
            String message
    ) {
        this.id = id;
        this.sourceName = sourceName;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.receivedCount = receivedCount;
        this.addedCount = addedCount;
        this.status = status;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public int getReceivedCount() {
        return receivedCount;
    }

    public int getAddedCount() {
        return addedCount;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return id + ". "
                + sourceName
                + " | started: " + startedAt
                + " | finished: " + finishedAt
                + " | received: " + receivedCount
                + " | added: " + addedCount
                + " | status: " + status
                + " | message: " + message;
    }
}