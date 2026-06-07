CREATE TABLE IF NOT EXISTS vacancies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    company VARCHAR(255),
    city VARCHAR(255),
    salary_from INT,
    salary_to INT,
    currency VARCHAR(20),
    description CLOB,
    source_url VARCHAR(1000) NOT NULL UNIQUE,
    published_at DATE
);