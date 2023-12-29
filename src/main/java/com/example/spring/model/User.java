package com.example.spring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @JoinColumn(name = "stripe_api", referencedColumnName = "id")
    private StripeUser stripe_api;

    @OneToOne
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    private UserSession session_id;

    public User() {
    }

    public User(String name, String email, String password, String phone, String city, String address, String role, Boolean verified, StripeUser stripe_api, UserSession session_id) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.city = city;
        this.address = address;
        this.role = role;
        this.verified = verified;
        this.stripe_api = stripe_api;
        this.session_id = session_id;
    }
}
