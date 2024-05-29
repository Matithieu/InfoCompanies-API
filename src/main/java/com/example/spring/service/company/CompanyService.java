package com.example.spring.service.company;

import com.example.spring.model.Company;
import com.example.spring.model.CompanyDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyService {

    Company getCompanyById(Long id);

    Company getCompanyByCompanyName(String companyName);

    void saveCompany(Company company);

    void deleteCompanyById(Long id);

    List<Company> getAmountOfCompanies(int amount);

    Page<CompanyDetails> searchCompanies(String name, Pageable pageable);

    Page<Company> getCompaniesByIndustrySectorAndRegion(String industrySector, String region, Pageable pageable);

    Page<Company> findRandomCompanies(Pageable pageable);

    Page<Company> getCompaniesByAListOfIds(List<Long> ids, Pageable pageable);

    Company scrapCompany(String companyName, String address);
}
