package com.example.spring.service.company;

import com.example.spring.model.Company;
import com.example.spring.model.CompanyDetails;
import com.example.spring.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not exist with id: " + id));
    }

    @Override
    public Company getCompanyByName(String name) {
        return companyRepository.findByDenomination(name);
    }

    @Override
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public void deleteCompanyById(Long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public List<Company> getAmountOfCompanies(int amount) {
        return companyRepository.findAll().subList(0, amount);
    }

    @Override
    public Page<CompanyDetails> searchCompanies(String name, Pageable pageable) {
        return companyRepository.findCompanyDetailsByDenomination(name, pageable);
    }

    @Override
    public Page<Company> getCompaniesBySecteurActiviteAndRegion(String secteurActivite, String region, Pageable pageable) {
        return companyRepository.findBySecteurActiviteContainingAndRegionContaining(secteurActivite, region, pageable);
    }

    @Override
    public Page<Company> findRandomCompanies(Pageable pageable) {
        return companyRepository.findRandomCompanies(pageable);
    }

    @Override
    public Page<Company> getCompaniesByAListOfIds(List<Long> ids, Pageable pageable) {
        return companyRepository.findAllByIdIn(ids, pageable);
    }
}