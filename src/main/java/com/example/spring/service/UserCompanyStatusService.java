package com.example.spring.service;

import com.example.spring.enums.Status;
import com.example.spring.model.UserCompanyStatus;
import com.example.spring.model.UserCompanyStatusId;
import com.example.spring.repository.UserCompanyStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCompanyStatusService {

    @Autowired
    private UserCompanyStatusRepository userCompanyStatusRepository;

    @Cacheable(value = "statuses", key = "#userId")
    public List<UserCompanyStatus> getCompaniesStatusesForUser(String userId) {
        return userCompanyStatusRepository.findByUserId(userId);
    }

    @CacheEvict(value = "statuses", key = "#userId")
    public void updateCompanyStatus(String userId, Long companyId, Status status) {
        UserCompanyStatusId id = new UserCompanyStatusId(userId, companyId);

        UserCompanyStatus ucs = userCompanyStatusRepository.findById(id).orElse(new UserCompanyStatus());
        ucs.setUserId(userId);
        ucs.setCompanyId(companyId);

        if (status == Status.NOT_DONE) {
            userCompanyStatusRepository.delete(ucs);
            return;
        }

        ucs.setStatus(status); // Set the status here
        userCompanyStatusRepository.save(ucs);
    }
}
