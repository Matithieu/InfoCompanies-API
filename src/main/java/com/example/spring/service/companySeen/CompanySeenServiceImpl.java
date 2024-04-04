package com.example.spring.service.companySeen;

import com.example.spring.model.CompanySeen;
import com.example.spring.repository.CompanySeenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanySeenServiceImpl implements CompanySeenService {

    @Autowired
    private CompanySeenRepository companySeenRepository;

    @Override
    public Optional<CompanySeen> getAllCompaniesSeen() {
        String email = "email";
        Long userId = 1L;
        return companySeenRepository.findById(userId);
    }

    @Override
    public CompanySeen getCompanySeenById(Long id) {
        return companySeenRepository.findById(id).orElseThrow(() -> new RuntimeException("CompanySeen not exist with id: " + id));
    }

/*    @Override
    public void updateCompanyIds(Long companyId, List<Long> newCompanyIds) {
        String email = "email";
        User user = new User();
        CompanySeen companySeen;

        // If the user has not seen any company yet
        if (user.getCompanySeen() == null) {
            companySeen = new CompanySeen();
            companySeen.setCompanyIds(newCompanyIds);
            companySeen.setUser(user);
            companySeenRepository.save(companySeen);
            return;
        }
        companySeenRepository.updateCompanyIds(companyId, newCompanyIds);
    }
 */

    @Override
    public CompanySeen saveCompanySeen(CompanySeen companySeen) {
        return companySeenRepository.save(companySeen);
    }

    @Override
    public void deleteCompanySeenById(Long id) {
        companySeenRepository.deleteById(id);
    }

    @Override
    public int getAmountOfCompanySeen() {
        int total = 0;
        List<CompanySeen> companySeenList = companySeenRepository.findAll();
        for (CompanySeen companySeen : companySeenList) {
            total += companySeen.getCompanyIds().size();
        }
        return total;
    }
}
