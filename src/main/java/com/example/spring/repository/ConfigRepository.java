package com.example.spring.repository;

import com.example.spring.model.Config;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConfigRepository extends CrudRepository<Config, Long> {
    Optional<Config> findTopByOrderByIdDesc();
}
