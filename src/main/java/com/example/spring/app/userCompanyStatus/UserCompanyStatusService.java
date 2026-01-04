package com.example.spring.app.userCompanyStatus;

import com.example.spring.app.company.enums.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCompanyStatusService {

    private final UserCompanyStatusRepository userCompanyStatusRepository;

    public UserCompanyStatusService(UserCompanyStatusRepository userCompanyStatusRepository) {
        this.userCompanyStatusRepository = userCompanyStatusRepository;
    }

    public UserCompanyStatusModel getOneUserCompanyStatusByUserIdAndCompanyId(String userId, Integer companyId) {
        return userCompanyStatusRepository.findUserCompanyStatusByUserIdAndCompanyId(userId, companyId);
    }

    public List<UserCompanyStatusModel> getMultipleUserCompanyStatusByUserIdAndCompanyIds(String userId, List<Integer> companyId) {
        return userCompanyStatusRepository.findByUserIdAndCompanyIdIn(userId, companyId);
    }

    //@CacheEvict(value = "statuses", key = "#userId")
    public UserCompanyStatusModel updateCompanyStatus(String userId, Integer companyId, Status status) {
        UserCompanyStatusModel uc = userCompanyStatusRepository.findUserCompanyStatusByUserIdAndCompanyId(userId, companyId);

        if (uc == null) {
            if (status == Status.NOT_DONE) {
                return null; // nothing to persist
            }

            UserCompanyStatusModel userCompanyStatus = UserCompanyStatusModel.builder()
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
