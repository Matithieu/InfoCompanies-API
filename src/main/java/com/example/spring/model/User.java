package com.example.spring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "email", length = 45)
    private String email;

    @Column(name = "password", length = 150)
    private String password;

    @Column(name = "phone", length = 45)
    private String phone;

    @Column(name = "city", length = 45)
    private String city;

    @Column(name = "address", length = 45)
    private String address;

    @Column(name = "role", length = 45)
    private String role;

    @Column(name = "verified", nullable = false)
    private boolean verified;

    @OneToOne
    @JoinColumn(name = "stripeId", referencedColumnName = "id")
    private StripeUser stripeId;

    @OneToOne
    @JoinColumn(name = "sessionId", referencedColumnName = "id")
    private UserSession sessionId;

    public User() {
    }

    public User(String name, String email, String password, String phone, String city, String address, String role, Boolean verified, StripeUser stripeId, UserSession sessionId) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.city = city;
        this.address = address;
        this.role = role;
        this.verified = verified;
        this.stripeId = stripeId;
        this.sessionId = sessionId;
    }
}
