package com.example.spring.repository;

import com.example.spring.model.UserCompanyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCompanyStatusRepository extends JpaRepository<UserCompanyStatus, Integer> {
    UserCompanyStatus findUserCompanyStatusByUserIdAndCompanyId(String userId, Integer companyId);

    List<UserCompanyStatus> findByUserIdAndCompanyIdIn(String userId, List<Integer> companyIds);
}
