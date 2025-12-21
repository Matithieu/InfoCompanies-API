package com.example.spring.app.company;

import com.example.spring.app.company.dto.CompanyDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends JpaRepository<CompanyModel, Integer>, JpaSpecificationExecutor<CompanyModel> {

    CompanyModel findCompanyById(Integer id);

    @Query("SELECT new com.example.spring.app.company.dto.CompanyDetails(c.id, c.companyName, c.industrySector, c.city, c.region) " +
            "FROM CompanyModel c WHERE LOWER(c.companyName) LIKE LOWER(CONCAT(:companyName, '%'))")
    Page<CompanyDetails> findCompanyDetailsByCompanyName(@Param("companyName") String companyName, Pageable pageable);


    // Specification
    Page<CompanyModel> findAll(Specification<CompanyModel> specification, Pageable pageable);

    @Query(value = "SELECT c.* FROM companies c " +
            "WHERE c.phone_number IS NOT NULL AND NOT " +
            "EXISTS (SELECT 1 FROM user_company_status ucs " +
            "WHERE ucs.company_id = c.id AND ucs.user_id = :userId)",
            nativeQuery = true)
    Page<CompanyModel> findRandomUnseenCompanies(@Param("userId") String userId, Pageable pageable);

    @Query(value = "SELECT c.* FROM companies c " +
            "INNER JOIN user_company_status ucs ON c.id = ucs.company_id " +
            "WHERE ucs.user_id = :userId", nativeQuery = true)
    Page<CompanyModel> findCompaniesSeenByUser(@Param("userId") String userId, Pageable pageable);
}