package com.example.spring.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "email", length = 45)
    private String email;
    @Column(name = "password", length = 45)
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean getVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public StripeUser getStripe_api() {
        return stripe_api;
    }

    public void setStripe_api(StripeUser stripe_api) {
        this.stripe_api = stripe_api;
    }

    public UserSession getSession_id() {
        return session_id;
    }

    public void setSession_id(UserSession session_id) {
        this.session_id = session_id;
    }
}
