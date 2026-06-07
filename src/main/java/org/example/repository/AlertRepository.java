package org.example.repository;

import org.example.domain.Alert;

import java.util.List;

public interface AlertRepository {
    void save(Alert alert);

    List<Alert> findAll();
}
