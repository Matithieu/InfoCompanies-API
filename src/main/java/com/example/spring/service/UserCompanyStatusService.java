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

    public UserCompanyStatus getOneUserCompanyStatusByUserIdAndCompanyId(String userId, Long companyId) {
        return userCompanyStatusRepository.findUserCompanyStatusByUserIdAndCompanyId(userId, companyId);
    }

    public List<UserCompanyStatus> getMultipleUserCompanyStatusByUserIdAndCompanyIds(String userId, List<Long> companyId) {
        return userCompanyStatusRepository.findByUserIdAndCompanyIdIn(userId, companyId);
    }

    //@CacheEvict(value = "statuses", key = "#userId")
    public void updateCompanyStatus(String userId, Long companyId, Status status) {
        UserCompanyStatus uc = userCompanyStatusRepository.findUserCompanyStatusByUserIdAndCompanyId(userId, companyId);

        if (uc == null) {
            if (status == Status.NOT_DONE) {
                return;
            }

            UserCompanyStatus userCompanyStatus = UserCompanyStatus.builder()
                    .userId(userId)
                    .status(status)
                    .companyId(companyId)
                    .build();
            userCompanyStatusRepository.save(userCompanyStatus);
            return;
        }

        if (uc.getCompanyId().equals(companyId)) {
            if (status == Status.NOT_DONE) {
                userCompanyStatusRepository.delete(uc);
                return;
            }

            uc.setStatus(status);
            userCompanyStatusRepository.save(uc);
        }
    }
}
