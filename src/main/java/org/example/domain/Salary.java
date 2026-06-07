package org.example.domain;

public class Salary {
    private final Integer from;
    private final Integer to;
    private final String currency;

    public Salary(Integer from, Integer to, String currency) {
        this.from = from;
        this.to = to;
        this.currency = currency;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean hasSalary() {
        return from != null || to != null;
    }

    public boolean isGreaterOrEqual(Integer minSalary) {
        if (minSalary == null) {
            return true;
        }

        if (from != null && from >= minSalary) {
            return true;
        }

        return to != null && to >= minSalary;
    }

    @Override
    public String toString() {
        if (!hasSalary()) {
            return "не указана";
        }

        if (from != null && to != null) {
            return from + " - " + to + " " + currency;
        }

        if (from != null) {
            return "от " + from + " " + currency;
        }

        return "до " + to + " " + currency;
    }
}
