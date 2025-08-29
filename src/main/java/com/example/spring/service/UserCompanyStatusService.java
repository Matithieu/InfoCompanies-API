package com.example.spring.service;

import com.example.spring.enums.Status;
import com.example.spring.model.UserCompanyStatus;
import com.example.spring.repository.UserCompanyStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCompanyStatusService {

    @Autowired
    private UserCompanyStatusRepository userCompanyStatusRepository;

    public UserCompanyStatus getOneUserCompanyStatusByUserIdAndCompanyId(String userId, Integer companyId) {
        return userCompanyStatusRepository.findUserCompanyStatusByUserIdAndCompanyId(userId, companyId);
    }

    public List<UserCompanyStatus> getMultipleUserCompanyStatusByUserIdAndCompanyIds(String userId, List<Integer> companyId) {
        return userCompanyStatusRepository.findByUserIdAndCompanyIdIn(userId, companyId);
    }

    //@CacheEvict(value = "statuses", key = "#userId")
    public UserCompanyStatus updateCompanyStatus(String userId, Integer companyId, Status status) {
        UserCompanyStatus uc = userCompanyStatusRepository.findUserCompanyStatusByUserIdAndCompanyId(userId, companyId);

        if (uc == null) {
            if (status == Status.NOT_DONE) {
                return null; // nothing to persist
            }

            UserCompanyStatus userCompanyStatus = UserCompanyStatus.builder()
                    .userId(userId)
                    .status(status)
                    .companyId(companyId)
                    .build();

            return userCompanyStatusRepository.save(userCompanyStatus);
        }

        if (status == Status.NOT_DONE) {
            userCompanyStatusRepository.delete(uc);
            return null; // deleted
        }

        uc.setStatus(status);
        return userCompanyStatusRepository.save(uc);
    }
}
