package com.example.spring.service;

import com.example.spring.DTO.User;
import com.example.spring.keycloakClient.UserResource;
import com.example.spring.model.UserQuota;
import com.example.spring.repository.UserQuotaRepository;
import com.example.spring.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@EnableScheduling
@Service
public class UserService {

    @Autowired
    private UserResource userResource;

    @Autowired
    private UserQuotaRepository userQuotaRepository;

    @Scheduled(fixedRate = 1000 * 60 * 15) // 15 minutes
    @Transactional
    public void assignQuotaToEmptyUsers() {
        try {
            List<User> users = userResource.getUsers();

            for (User user : users) {
                Optional<UserQuota> userQuota = userQuotaRepository.findByUserId(user.getId());
                if (userQuota.isEmpty()) {
                    UserQuota newUserQuota = new UserQuota();
                    newUserQuota.setUserId(user.getId());
                    newUserQuota.setQuotaAllocated(100);
                    newUserQuota.setQuotaUsed(0);
                    userQuotaRepository.save(newUserQuota);

                    LogUtil.info("Assigned new quota to user: ", Map.of(
                            "userId", user.getId()
                    ));
                }
            }
        } catch (Exception e) {
            LogUtil.error("Error while assigning quota to empty users:", e);
        }
    }
}
