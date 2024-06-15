package com.example.spring.service;

import com.example.spring.model.CompanySeen;
import com.example.spring.repository.CompanySeenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanySeenService {

    @Autowired
    private CompanySeenRepository companySeenRepository;

    public Optional<CompanySeen> getAllCompaniesSeen(String userId) {
        return companySeenRepository.findByUserId(userId);
    }

    public CompanySeen getCompanySeenById(Long id) {
        return companySeenRepository.findById(id).orElseThrow(() -> new RuntimeException("CompanySeen not exist with id: " + id));
    }

    public CompanySeen saveCompanySeen(String userId, List<Long> companyIds) {
        CompanySeen companySeen = companySeenRepository.findByUserId(userId).orElse(new CompanySeen());
        companySeen.setUserId(userId);
        companySeen.setCompanyIds(companyIds);
        return companySeenRepository.save(companySeen);
    }

    public void updateCompanySeen(String userId, List<Long> companyIds) {
        CompanySeen companySeen = companySeenRepository.findByUserId(userId).orElse(new CompanySeen());
        companySeen.setUserId(userId);

        Set<Long> existingCompanyIds = new HashSet<>(companySeen.getCompanyIds());
        for (Long companyId : companyIds) {
            if (existingCompanyIds.contains(companyId)) {
                existingCompanyIds.remove(companyId);
            } else {
                existingCompanyIds.add(companyId);
            }
        }
        companySeen.setCompanyIds(new ArrayList<>(existingCompanyIds));
        companySeenRepository.save(companySeen);
    }


    public void deleteCompanySeenById(Long id) {
        companySeenRepository.deleteById(id);
    }

    public int getAmountOfCompanySeen(String userId) {
        Optional<CompanySeen> companySeen = companySeenRepository.findByUserId(userId);
        return companySeen.map(value -> value.getCompanyIds().size()).orElse(0);
    }
}
