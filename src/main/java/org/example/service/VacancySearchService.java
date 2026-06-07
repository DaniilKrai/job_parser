package org.example.service;

import org.example.domain.SearchCriteria;
import org.example.domain.Vacancy;
import org.example.repository.VacancyRepository;

import java.util.List;

public class VacancySearchService {
    private final VacancyRepository vacancyRepository;

    public VacancySearchService(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    public List<Vacancy> search (SearchCriteria criteria) {
        return vacancyRepository.findAll()
                .stream()
                .filter(vacancy -> matchesKeyword(vacancy, criteria.getKeyword()))
                .filter(vacancy -> matchesCity(vacancy, criteria.getCity()))
                .filter(vacancy -> matchesCompany(vacancy, criteria.getCompany()))
                .filter(vacancy -> matchesSalary(vacancy, criteria.getMinSalary()))
                .toList();
    }

    private boolean matchesKeyword(Vacancy vacancy, String keyword) {
        return keyword == null || keyword.isBlank() || vacancy.containsKeyword(keyword);
    }

    private boolean matchesCity(Vacancy vacancy, String city) {
        return city == null
                || city.isBlank()
                || vacancy.getCity().equalsIgnoreCase(city);
    }

    private boolean matchesCompany(Vacancy vacancy, String company) {
        return company == null
                || company.isBlank()
                || vacancy.getCompany().equalsIgnoreCase(company);
    }

    private boolean matchesSalary(Vacancy vacancy, Integer minSalary) {
        return minSalary == null
                || vacancy.getSalary().isGreaterOrEqual(minSalary);
    }

}
