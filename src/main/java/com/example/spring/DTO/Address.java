package com.example.spring.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    private String street;
    private String locality;
    private String region;
    private String postalCode;
    private String country;
}
