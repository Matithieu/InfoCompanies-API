package com.example.spring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String sirenNumber;
    private String nicNumber;
    private String legalForm;
    @Column(name = "ape_code")
    private String apeCode;
    private String address;
    private String postalCode;
    private String city;
    private String region;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deregistrationDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2018_1;
    private Double revenue_2018_1;
    private Double turnover_2018_1;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2018_2;
    private Double revenue_2018_2;
    private Double turnover_2018_2;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2018_3;
    private Double revenue_2018_3;
    private Double turnover_2018_3;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2019_1;
    private Double revenue_2019_1;
    private Double turnover_2019_1;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2019_2;
    private Double revenue_2019_2;
    private Double turnover_2019_2;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2019_3;
    private Double revenue_2019_3;
    private Double turnover_2019_3;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2020_1;
    private Double revenue_2020_1;
    private Double turnover_2020_1;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2020_2;
    private Double revenue_2020_2;
    private Double turnover_2020_2;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2020_3;
    private Double revenue_2020_3;
    private Double turnover_2020_3;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2021_1;
    private Double revenue_2021_1;
    private Double turnover_2021_1;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2021_2;
    private Double revenue_2021_2;
    private Double turnover_2021_2;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2021_3;
    private Double revenue_2021_3;
    private Double turnover_2021_3;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2022_1;
    private Double revenue_2022_1;
    private Double turnover_2022_1;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2022_2;
    private Double revenue_2022_2;
    private Double turnover_2022_2;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2022_3;
    private Double revenue_2022_3;
    private Double turnover_2022_3;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2023_1;
    private Double revenue_2023_1;
    private Double turnover_2023_1;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2023_2;
    private Double revenue_2023_2;
    private Double turnover_2023_2;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingDate_2023_3;
    private Double revenue_2023_3;
    private Double turnover_2023_3;

    private String industrySector;
    private String phoneNumber;
    @Column(length = 3000)
    private String website;
    @Column(length = 10000)
    private String reviews;
    @Column(length = 100000)
    private String schedule;
    @Column(length = 10000)
    private String instagram;
    @Column(length = 3000)
    private String facebook;
    @Column(length = 3000)
    private String twitter;
    @Column(length = 3000)
    private String linkedin;
    @Column(length = 3000)
    private String youtube;
    @Column(length = 3000)
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate scrapingDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateCreation;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastProcessingDate;
    private String numberOfEmployee;
    private String companyCategory;
}
