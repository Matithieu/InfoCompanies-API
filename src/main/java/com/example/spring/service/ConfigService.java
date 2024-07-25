package com.example.spring.service;

import com.example.spring.model.Config;
import com.example.spring.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class ConfigService {

    private final ConfigRepository configRepository;

    @Autowired
    public ConfigService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public Config getLastConfig() {
        return configRepository.findTopByOrderByIdDesc()
                .orElseGet(this::createDefaultConfig);
    }

    private Config createDefaultConfig() {
        Config defaultConfig = new Config();
        // Set default values here
        defaultConfig.setLastResetQuotaDate(LocalDate.now(ZoneId.systemDefault()));
        configRepository.save(defaultConfig);
        return defaultConfig;
    }
}
