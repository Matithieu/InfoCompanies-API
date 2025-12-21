package com.example.spring.app.user;

import com.example.spring.core.keycloakClient.UserResource;
import com.example.spring.core.userQuota.UserQuotaModel;
import com.example.spring.core.userQuota.UserQuotaRepository;
import com.example.spring.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.spring.core.userQuota.UserQuotaUtil.getRemainingSearchesBasedOnUserTier;

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
            List<UserDTO> users = userResource.getUsers();

            for (UserDTO user : users) {
                Optional<UserQuotaModel> userQuota = userQuotaRepository.findByUserId(user.getId());
                if (userQuota.isEmpty()) {
                    UserQuotaModel newUserQuota = new UserQuotaModel();
                    newUserQuota.setUserId(user.getId());
                    Integer quotaAllocated = getRemainingSearchesBasedOnUserTier(user);

                    newUserQuota.setQuotaAllocated(quotaAllocated);
                    newUserQuota.setQuotaUsed(0);
                    userQuotaRepository.save(newUserQuota);

                    LogUtil.info("Assigned new quota to user", Map.of(
                            "userId", user.getId(),
                            "quotaAllocated", quotaAllocated
                    ));
                }
            }
        } catch (Exception e) {
            LogUtil.error("Error while assigning quota to empty users:", e);
        }
    }
}
