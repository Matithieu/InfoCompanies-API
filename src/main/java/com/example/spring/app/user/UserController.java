package com.example.spring.app.user;

import com.example.spring.core.keycloakClient.UserResource;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.spring.common.utils.JwtUtil.extractUserIdFromHeader;

@CrossOrigin
@RestController
@RequestMapping("/v1/user")

public class UserController {

    @Autowired
    UserResource userResource;

    @GetMapping("/get-user")
    public UserDTO getUser() {
        String userId = extractUserIdFromHeader();
        return userResource.getUserById(userId);
    }

    @PostMapping("/complete-onboarding")
    public Response completeOnboarding() {
        String userId = extractUserIdFromHeader();
        userResource.completeOnboarding(userId);
        return Response.ok().build();
    }

    @PutMapping("/update-user")
    public Response updateUser(@RequestParam UserDTO user) {
        String id = extractUserIdFromHeader();
        UserDTO existingUser = userResource.getUserById(id);

        if (existingUser != null) {
            // Ensure that the user's ID and verified status are not changed
            user.setId(existingUser.getId());
            user.setVerified(existingUser.isVerified());
            user.setTier(existingUser.getTier());

            userResource.updateUser(user);
            return Response.ok().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
