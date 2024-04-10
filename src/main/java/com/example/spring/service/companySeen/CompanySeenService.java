package com.example.spring.service.companySeen;

import com.example.spring.model.CompanySeen;

import java.util.List;
import java.util.Optional;

public interface CompanySeenService {

    Optional<CompanySeen> getAllCompaniesSeen();

    CompanySeen getCompanySeenById(Long id);

//    void updateCompanyIds(Long companyId, List<Long> newCompanyIds);

    CompanySeen saveCompanySeen(CompanySeen companySeen);

    void deleteCompanySeenById(Long id);

    int getAmountOfCompanySeen();
}
