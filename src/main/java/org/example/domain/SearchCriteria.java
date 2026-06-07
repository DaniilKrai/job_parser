package org.example.domain;

public class SearchCriteria {
    private final String keyword;
    private final String city;
    private final String company;
    private final Integer minSalary;

    public SearchCriteria(
            String keyword,
            String city,
            String company,
            Integer minSalary) {
        this.keyword = keyword;
        this.city = city;
        this.company = company;
        this.minSalary = minSalary;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getCity() {
        return city;
    }

    public String getCompany() {
        return company;
    }

    public Integer getMinSalary() {
        return minSalary;
    }

    public static SearchCriteria byKeyword(String keyword) {
        return new SearchCriteria(keyword, null, null, null);
    }

    public static SearchCriteria byCity(String city) {
        return new SearchCriteria(null, city, null, null);
    }

    public static SearchCriteria byCompany(String company) {
        return new SearchCriteria(null, null, company, null);
    }

    public static SearchCriteria byMinSalary(Integer minSalary) {
        return new SearchCriteria(null, null, null, minSalary);
    }
}
