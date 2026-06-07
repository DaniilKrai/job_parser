package org.example.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalaryTest {

    @Test
    void shouldReturnTrueWhenSalaryFromIsGreaterThanMinSalary() {
        Salary salary = new Salary(150000, 250000, "RUB");

        boolean result = salary.isGreaterOrEqual(100000);

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenSalaryToIsGreaterThanMinSalary() {
        Salary salary = new Salary(null, 80000, "RUB");

        boolean result = salary.isGreaterOrEqual(70000);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenSalaryIsLessThanMinSalary() {
        Salary salary = new Salary(50000, 80000, "RUB");

        boolean result = salary.isGreaterOrEqual(100000);

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenMinSalaryIsNull() {
        Salary salary = new Salary(50000, 80000, "RUB");

        boolean result = salary.isGreaterOrEqual(null);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenSalaryIsNotSpecified() {
        Salary salary = new Salary(null, null, "RUB");

        boolean result = salary.isGreaterOrEqual(100000);

        assertFalse(result);
    }
}