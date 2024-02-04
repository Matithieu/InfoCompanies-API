package com.example.spring.repository;


import com.example.spring.model.Role;
import com.example.spring.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(RoleName roleName);

    boolean existsByRoleName (RoleName roleName);
}