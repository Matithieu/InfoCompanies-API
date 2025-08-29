package com.example.spring.dto;

import com.example.spring.dto.company.Contact;
import com.example.spring.dto.company.SocialMedia;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyFilterRequest {
    private List<String> regionNames;
    private List<String> cityNames;
    private List<String> industrySectorNames;
    private List<String> legalFormNames;
    private NumberOfEmployeeFilter numberOfEmployeeFilter;
    private SocialMedia socials;
    private Contact contacts;
    private Boolean isCompanySeen;
    private Integer page;
    private Integer size;
}
