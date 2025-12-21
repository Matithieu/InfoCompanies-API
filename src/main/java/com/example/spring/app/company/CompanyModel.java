package com.example.spring.app.company;

import com.example.spring.app.company.dto.CompanyDTO;
import com.example.spring.app.company.objects.ContactDTO;
import com.example.spring.app.company.objects.SocialMediaDTO;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Map;

import static com.example.spring.app.company.financial.FinancialPeriodMapper.toFinancialPeriodDTOList;
import static com.example.spring.app.company.CompanyUtil.maskData;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "companies")
public class CompanyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String companyName;
    private String sirenNumber;
    private String nicNumber;
    private String legalForm;
    @Column(name = "ape_code")
    private String apeCode;
    private String apeLabel;
    private String address;
    private String postalCode;
    private String departmentNumber;
    private String department;
    private String city;
    private String region;
    private String tradeName;

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

    @Type(JsonBinaryType.class)
    @Column(length = 100000, columnDefinition = "jsonb")
    private Map<String, Object> reviews;

    @Type(JsonBinaryType.class)
    @Column(length = 100000, columnDefinition = "jsonb")
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
    private Integer numberOfEmployee;
    private String companyCategory;

    public void updateFrom(CompanyModel other) {
        this.phoneNumber = other.getPhoneNumber();
        this.website = other.getWebsite();
        this.instagram = other.getInstagram();
        this.facebook = other.getFacebook();
        this.twitter = other.getTwitter();
        this.linkedin = other.getLinkedin();
        this.youtube = other.getYoutube();
        this.email = other.getEmail();
        this.scrapingDate = other.getScrapingDate();
        this.reviews = other.getReviews();
        this.schedule = other.getSchedule();
    }

    public void obstructCompany() {
        this.email = maskData(this.getEmail());
        this.phoneNumber = maskData(this.getPhoneNumber());
        this.instagram = maskData(this.getInstagram());
        this.facebook = maskData(this.getFacebook());
        this.twitter = maskData(this.getTwitter());
        this.linkedin = maskData(this.getLinkedin());
        this.youtube = maskData(this.getYoutube());
    }

    public CompanyDTO toCompanyDTO() {
        return CompanyDTO.builder()
                .id(this.getId())
                .companyName(this.getCompanyName())
                .sirenNumber(this.getSirenNumber())
                .nicNumber(this.getNicNumber())
                .legalForm(this.getLegalForm())
                .apeCode(this.getApeCode())
                .apeLabel(this.getApeLabel())
                .address(this.getAddress())
                .postalCode(this.getPostalCode())
                .departmentNumber(this.getDepartmentNumber())
                .department(this.getDepartment())
                .city(this.getCity())
                .region(this.getRegion())
                .tradeName(this.getTradeName())
                .registrationDate(this.getRegistrationDate())
                .deregistrationDate(this.getDeregistrationDate())
                .industrySector(this.getIndustrySector())
                .reviews(this.getReviews())
                .schedule(this.getSchedule())
                .socialMedia(
                        SocialMediaDTO.builder()
                                .instagram(this.getInstagram())
                                .facebook(this.getFacebook())
                                .twitter(this.getTwitter())
                                .linkedin(this.getLinkedin())
                                .youtube(this.getYoutube())
                                .build()
                )
                .contact(
                        ContactDTO.builder()
                                .email(this.getEmail())
                                .phoneNumber(this.getPhoneNumber())
                                .website(this.getWebsite())
                                .build()
                )
                .scrapingDate(this.getScrapingDate())
                .dateCreation(this.getDateCreation())
                .lastProcessingDate(this.getLastProcessingDate())
                .numberOfEmployee(this.getNumberOfEmployee())
                .companyCategory(this.getCompanyCategory())
                .financialPeriods(toFinancialPeriodDTOList(this))
                .build();
    }
}
