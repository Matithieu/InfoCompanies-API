package com.example.spring.repository;

import com.example.spring.model.UserCompanyStatus;
import com.example.spring.model.UserCompanyStatusId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCompanyStatusRepository extends JpaRepository<UserCompanyStatus, UserCompanyStatusId> {
    List<UserCompanyStatus> findByUserId(String userId);
}
