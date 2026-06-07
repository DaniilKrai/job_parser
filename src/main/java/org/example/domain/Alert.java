package org.example.domain;

import java.time.LocalDateTime;

public class Alert {
    private final Long id;
    private final String keyword;
    private final String city;
    private final Integer minSalary;
    private final LocalDateTime createdAt;

    public Alert(Long id, String keyword, String city, Integer minSalary, LocalDateTime createdAt) {
        this.id = id;
        this.keyword = keyword;
        this.city = city;
        this.minSalary = minSalary;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getCity() {
        return city;
    }

    public Integer getMinSalary() {
        return minSalary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return id + ". keyword=" + keyword
                + " | city=" + city
                + " | minSalary=" + minSalary
                + " | createdAt=" + createdAt;
    }
}
