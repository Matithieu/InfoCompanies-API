package com.example.spring.repository;

import com.example.spring.model.Company;
import com.example.spring.model.CompanyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findCompanyById(Long id);

    Company findByCompanyName(String denomination);

    @Query("SELECT new com.example.spring.model.CompanyDetails(c.id, c.companyName, c.industrySector, c.city, c.region) " +
            "FROM Company c WHERE LOWER(c.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))")
    Page<CompanyDetails> findCompanyDetailsByCompanyName(@Param("companyName") String companyName, Pageable pageable);

    Page<Company> findByIndustrySectorContainingAndRegionContaining(String industrySector, String region, Pageable pageable);

    @Query(value = "SELECT * FROM companies WHERE companies.phone_number IS NOT NULL ORDER BY RANDOM()",
            countQuery = "SELECT COUNT(*) FROM companies WHERE companies.phone_number IS NOT NULL", nativeQuery = true)
    Page<Company> findRandomCompanies(Pageable pageable);

    Page<Company> findAllByIdIn(List<Long> ids, Pageable pageable);

    @Query(value = "SELECT c.* FROM companies c WHERE NOT EXISTS " +
            "(SELECT 1 FROM company_seen_company_ids csci JOIN company_seen cs ON csci.company_seen_id = cs.id " +
            "WHERE csci.company_ids = c.id AND cs.user_id = :userId) AND c.phone_number IS NOT NULL",
            countQuery = "SELECT COUNT(*) FROM companies c WHERE NOT EXISTS " +
                    "(SELECT 1 FROM company_seen_company_ids csci JOIN company_seen cs ON csci.company_seen_id = cs.id " +
                    "WHERE csci.company_ids = c.id AND cs.user_id = :userId) AND c.phone_number IS NOT NULL", nativeQuery = true)
    Page<Company> findRandomSeenCompanies(@Param("userId") String userId, Pageable pageable);
}