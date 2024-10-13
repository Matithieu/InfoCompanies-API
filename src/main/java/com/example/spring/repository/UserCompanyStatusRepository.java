package com.example.spring.repository;

import com.example.spring.model.UserCompanyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCompanyStatusRepository extends JpaRepository<UserCompanyStatus, Long> {
    UserCompanyStatus findUserCompanyStatusByUserIdAndCompanyId(String userId, Long companyId);

    List<UserCompanyStatus> findByUserIdAndCompanyIdIn(String userId, List<Long> companyIds);
}
