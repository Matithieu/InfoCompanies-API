package com.example.spring.controller;

import com.example.spring.model.Role;
import com.example.spring.model.RoleName;
import com.example.spring.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialDataConfig {

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public CommandLineRunner initialDataLoader() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                createRolesIfNotExists();
            }
        };
    }

    private void createRolesIfNotExists() {
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByRoleName(roleName)) {
                Role role = new Role(roleName);
                roleRepository.save(role);
            }
        }
    }
}
