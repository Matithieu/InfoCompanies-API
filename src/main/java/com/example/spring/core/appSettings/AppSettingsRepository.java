package com.example.spring.core.appSettings;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AppSettingsRepository extends CrudRepository<AppSettings, Integer> {
    Optional<AppSettings> findTopByOrderByIdDesc();
}
