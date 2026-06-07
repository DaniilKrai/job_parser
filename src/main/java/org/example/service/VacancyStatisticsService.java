package org.example.service;

import org.example.domain.Salary;
import org.example.domain.Vacancy;
import org.example.repository.VacancyRepository;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VacancyStatisticsService {
    private final VacancyRepository vacancyRepository;

    public VacancyStatisticsService(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    public Map<String, Long> countByCity() {
        return vacancyRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Vacancy::getCity,
                        Collectors.counting()
                ));
    }

    public Map<String, Long> countByCompany() {
        return vacancyRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Vacancy::getCompany,
                        Collectors.counting()
                ));
    }

    public List<Vacancy> topBySalary(int limit) {
        return vacancyRepository.findAll()
                .stream()
                .filter(vacancy -> vacancy.getSalary().hasSalary())
                .sorted(Comparator.comparingInt(this::getMaxSalary).reversed())
                .limit(limit)
                .toList();
    }

    public double averageSalary() {
        List<Integer> salaries = vacancyRepository.findAll()
                .stream()
                .map(Vacancy::getSalary)
                .filter(Salary::hasSalary)
                .map(this::getSalaryValue)
                .toList();

        if (salaries.isEmpty()) {
            return 0;
        }

        return salaries.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }

    private int getSalaryValue(Salary salary) {
        if (salary.getFrom() != null && salary.getTo() != null) {
            return (salary.getFrom() + salary.getTo()) / 2;
        }

        if (salary.getFrom() != null) {
            return salary.getFrom();
        }

        return salary.getTo();
    }

    private int getMaxSalary(Vacancy vacancy) {
        Salary salary = vacancy.getSalary();

        if (salary.getTo() != null) {
            return salary.getTo();
        }

        if (salary.getFrom() != null) {
            return salary.getFrom();
        }

        return 0;
    }

    public Map<String, Long> sortByValueDesc(Map<String, Long> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }
}