package com.example.spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetails implements Serializable {
    private Long id;
    private String companyName;
    private String industrySector;
    private String city;
    private String region;
}
