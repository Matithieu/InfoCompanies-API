package com.example.spring.dto.company;

import com.example.spring.dto.company.financial.FinancialPeriodDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDTO {
    private Integer id;
    private String companyName;
    private String sirenNumber;
    private String nicNumber;
    private String legalForm;
    private String apeCode;
    private String apeLabel;
    private String address;
    private String postalCode;
    private String departmentNumber;
    private String department;
    private String city;
    private String region;
    private String tradeName;
    private LocalDate registrationDate;
    private LocalDate deregistrationDate;
    private String industrySector;
    private String phoneNumber;
    private String website;
    private Map<String, Object> reviews;
    private String schedule;
    private String instagram;
    private String facebook;
    private String twitter;
    private String linkedin;
    private String youtube;
    private String email;
    private LocalDate scrapingDate;
    private LocalDate dateCreation;
    private LocalDate lastProcessingDate;
    private Integer numberOfEmployee;
    private String companyCategory;

    private List<FinancialPeriodDTO> financialPeriods;
}
