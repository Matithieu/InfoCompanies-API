package com.example.spring.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private String phone;
    private String street;
    private String locality;
    private String region;
    private String postalCode;
    private String country;
    private TierUser tier;
    private boolean isVerified;
    private boolean hasCompletedOnboarding;
}

