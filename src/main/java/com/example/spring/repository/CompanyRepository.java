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

    Company findByCompanyName(String denomination);

    Page<Company> findByCompanyName(String nom, Pageable pageable);

    @Query("SELECT new com.example.spring.model.CompanyDetails(c.id, c.companyName, c.industrySector, c.city, c.region) " +
            "FROM Company c WHERE LOWER(c.companyName) LIKE LOWER(CONCAT('%', :denomination, '%'))")
    Page<CompanyDetails> findCompanyDetailsByDenomination(@Param("denomination") String denomination, Pageable pageable);
    Page<Company> findByIndustrySectorContainingAndRegionContaining(String industrySector, String region, Pageable pageable);

    @Query(value = "SELECT * FROM Company WHERE phone IS NOT NULL ORDER BY RANDOM()",
            countQuery = "SELECT COUNT(*) FROM Company WHERE phone IS NOT NULL", nativeQuery = true)
    Page<Company> findRandomCompanies(Pageable pageable);

    Page<Company> findAllByIdIn(List<Long> ids, Pageable pageable);
}
