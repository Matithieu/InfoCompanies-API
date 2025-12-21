package com.example.spring.app.userCompanyStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCompanyStatusRepository extends JpaRepository<UserCompanyStatusModel, Integer> {
    UserCompanyStatusModel findUserCompanyStatusByUserIdAndCompanyId(String userId, Integer companyId);

    List<UserCompanyStatusModel> findByUserIdAndCompanyIdIn(String userId, List<Integer> companyIds);
}
