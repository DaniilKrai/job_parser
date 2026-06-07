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

CREATE TABLE IF NOT EXISTS fetch_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    source_name VARCHAR(255) NOT NULL,
    started_at TIMESTAMP NOT NULL,
    finished_at TIMESTAMP NOT NULL,
    received_count INT NOT NULL,
    added_count INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    message VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    keyword VARCHAR(255),
    city VARCHAR(255),
    min_salary INT,
    created_at TIMESTAMP NOT NULL
);
