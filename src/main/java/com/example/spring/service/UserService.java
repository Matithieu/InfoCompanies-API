package com.example.spring.service;

import com.example.spring.DTO.User;
import com.example.spring.keycloakClient.UserResource;
import com.example.spring.model.UserQuota;
import com.example.spring.repository.UserQuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@EnableScheduling
@Service
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserResource userResource;

    @Autowired
    private UserQuotaRepository userQuotaRepository;

    @Scheduled(fixedRate = 1000 * 60 * 5) // 5 minutes
    @Transactional
    public void assignQuotaToEmptyUsers() {
        LOGGER.log(Level.INFO, "Assigning quota to empty users");
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
                    LOGGER.log(Level.INFO, "Assigned new quota to user with ID: {0}", user.getId());
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while assigning quota to empty users", e);
        }
    }
}
