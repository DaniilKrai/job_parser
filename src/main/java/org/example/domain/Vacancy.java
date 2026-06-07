package org.example.domain;

import java.time.LocalDate;

public class Vacancy {
    private final String title;
    private final String company;
    private final String city;
    private final Salary salary;
    private final String description;
    private final String sourceUrl;
    private final LocalDate publishedAt;

    public Vacancy(
            String title,
            String company,
            String city,
            Salary salary,
            String description,
            String sourceUrl,
            LocalDate publishedAt
    ) {
        this.title = title;
        this.company = company;
        this.city = city;
        this.salary = salary;
        this.description = description;
        this.sourceUrl = sourceUrl;
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getCity() {
        return city;
    }

    public Salary getSalary() {
        return salary;
    }

    public String getDescription() {
        return description;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public LocalDate getPublishedAt() {
        return publishedAt;
    }

    public boolean containsKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }

        String lowerKeyword = keyword.toLowerCase();

        return title.toLowerCase().contains(lowerKeyword)
                || description.toLowerCase().contains(lowerKeyword);
    }

    @Override
    public String toString() {
        return title + " | "
                + company + " | "
                + city + " | "
                + salary + " | "
                + publishedAt;
    }
}