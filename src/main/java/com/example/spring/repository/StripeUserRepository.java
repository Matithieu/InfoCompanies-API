package com.example.spring.repository;

import com.example.spring.model.StripeUser;
import com.example.spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StripeUserRepository extends JpaRepository<StripeUser, Long> {
}
