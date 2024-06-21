package com.example.spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CompanyDetails {

    private Long id;
    private String companyName;
    private String industrySector;
    private String city;
    private String region;
}
