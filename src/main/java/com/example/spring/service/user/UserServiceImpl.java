package com.example.spring.service.user;

import com.example.spring.controller.DTO.BearerToken;
import com.example.spring.controller.DTO.LoginDTO;
import com.example.spring.controller.DTO.RegisterDTO;
import com.example.spring.controller.DTO.ResponseDTO;
import com.example.spring.model.Role;
import com.example.spring.model.RoleName;
import com.example.spring.model.User;
import com.example.spring.model.UserQuota;
import com.example.spring.repository.RoleRepository;
import com.example.spring.repository.UserRepository;
import com.example.spring.security.jwt.JwtUtilities;
import com.example.spring.service.userQuota.UserQuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserQuotaService userQuotaService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtilities jwtUtilities;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean hasUserAnEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public ResponseEntity<?> register(RegisterDTO registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>("email is already taken !", HttpStatus.SEE_OTHER);
        } else {
            User user = new User();
            UserQuota userQuota = new UserQuota();
            Role role = roleRepository.findByRoleName(RoleName.USER);

            userQuota.setRemainingSearches(1000);
            userQuota.setTotalSearchesAllowed(1000);

            userQuotaService.saveUserQuota(userQuota);

            user.setEmail(registerDto.getEmail());
            user.setName(registerDto.getName());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            user.setRoles(Collections.singletonList(role));
            user.setUserQuota(userQuota);
            userRepository.save(user);

            String token = jwtUtilities.generateToken(registerDto.getEmail(), Collections.singletonList(role.getRoleName()));
            BearerToken bearerToken = new BearerToken(token, "Bearer ");
            return new ResponseEntity<>(new ResponseDTO(
                    bearerToken,
                    userRepository.findByEmail(registerDto.getEmail())
            ), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> authenticate(LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByEmail(authentication.getName());
        List<String> rolesNames = new ArrayList<>();
        user.getRoles().forEach(r -> rolesNames.add(r.getRoleName()));
        String token = jwtUtilities.generateToken(user.getEmail(), rolesNames);
        BearerToken bearerToken = new BearerToken(token, "Bearer ");
        return new ResponseEntity<>(new ResponseDTO(
                bearerToken,
                userRepository.findByEmail(loginDto.getEmail())
        ), HttpStatus.OK);
    }
}