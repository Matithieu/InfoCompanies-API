package com.example.spring.repository;

import com.example.spring.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findByDenomination(String denomination);

    Page<Company> findByDenominationContaining(String nom, Pageable pageable);

}
