package com.example.spring.repository;

import com.example.spring.model.CompanySeen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CompanySeenRepository extends JpaRepository<CompanySeen, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE CompanySeen SET companyIds = :newCompanyIds WHERE id = :companyId", nativeQuery = true)
    void updateCompanyIds(@Param("companyId") Long companyId, @Param("newCompanyIds") List<Long> newCompanyIds);
}
