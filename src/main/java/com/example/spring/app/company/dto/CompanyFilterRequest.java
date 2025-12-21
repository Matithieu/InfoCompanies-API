package com.example.spring.app.company.dto;

import com.example.spring.app.company.objects.ContactDTO;
import com.example.spring.app.company.objects.SocialMediaDTO;
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
    private NumberOfEmployeeFilterDTO numberOfEmployeeFilter;
    private SocialMediaDTO socials;
    private ContactDTO contacts;
    private Boolean isCompanySeen;
    private Integer page;
    private Integer size;
}
