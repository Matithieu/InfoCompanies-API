package com.example.spring.service.userQuota;

import com.example.spring.model.UserQuota;
import com.example.spring.repository.UserQuotaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserQuotaServiceImpl implements UserQuotaService {

    private final UserQuotaRepository userQuotaRepository;

    public boolean hasRemainingSearches(Long id) {
        UserQuota userQuota = userQuotaRepository.findUserQuotaById(id);
        return userQuota != null && userQuota.getRemainingSearches() > 0;
    }

    public void decrementRemainingSearches(Long id) {
        UserQuota userQuota = userQuotaRepository.findUserQuotaById(id);
        if (userQuota != null) {
            userQuota.setRemainingSearches(userQuota.getRemainingSearches() - 1);
            userQuotaRepository.save(userQuota);
        }
    }

    @Override
    public void saveUserQuota(UserQuota userQuota) {
        userQuotaRepository.save(userQuota);
    }
}
