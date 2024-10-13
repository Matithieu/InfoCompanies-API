package com.example.spring.utils;

import com.example.spring.DTO.CompanyWithStatusDTO;
import com.example.spring.model.Company;
import com.example.spring.model.UserCompanyStatus;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CompanyUtil {

    public static Page<Company> obstructCompanies(Page<Company> companies) {
        List<Company> obstructedCompanies = companies.getContent().stream()
                .map(CompanyUtil::obstructCompany)
                .collect(Collectors.toList());

        return new PageImpl<>(obstructedCompanies, companies.getPageable(), companies.getTotalElements());
    }

    private static Company obstructCompany(Company company) {
        company.setEmail(maskData(company.getEmail()));
        company.setPhoneNumber(maskData(company.getPhoneNumber()));
        company.setInstagram(maskData(company.getInstagram()));
        company.setFacebook(maskData(company.getFacebook()));
        company.setTwitter(maskData(company.getTwitter()));
        company.setLinkedin(maskData(company.getLinkedin()));
        company.setYoutube(maskData(company.getYoutube()));

        return company;
    }

    private static String maskData(String data) {
        if (data == null) {
            return null;
        }
        return data.substring(0, 3) + data.substring(3).replaceAll("[a-zA-Z0-9]", "*");
    }

    public static void setIfNotEmpty(JsonNode jsonNode, String fieldName, Consumer<String> setter) {
        if (jsonNode.hasNonNull(fieldName) && !jsonNode.get(fieldName).asText().isEmpty()) {
            setter.accept(jsonNode.get(fieldName).asText());
        }
    }

    public static Page<CompanyWithStatusDTO> fillPageCompanyWithStatusDto(Page<Company> companiesPage, List<UserCompanyStatus> userCompanyStatuses) {
        // Fill the CompanyWithStatusDTO with the company and its status
        Page<CompanyWithStatusDTO> companyWithStatusDTOS;

        if (userCompanyStatuses.isEmpty()) {
            companyWithStatusDTOS = new PageImpl<>(companiesPage.getContent().stream()
                    .map(company -> new CompanyWithStatusDTO(company, null))
                    .collect(Collectors.toList()), companiesPage.getPageable(), companiesPage.getTotalElements());
        } else {
            companyWithStatusDTOS = new PageImpl<>(companiesPage.getContent().stream()
                    .map(company -> new CompanyWithStatusDTO(company, userCompanyStatuses.stream()
                            .filter(userCompanyStatus -> userCompanyStatus.getCompanyId().equals(company.getId()))
                            .findFirst()
                            .orElse(null)))
                    .collect(Collectors.toList()), companiesPage.getPageable(), companiesPage.getTotalElements());
        }

        return companyWithStatusDTOS;
    }

    public static CompanyWithStatusDTO fillCompanyWithStatusDto(Company company, UserCompanyStatus userCompanyStatus) {
        return new CompanyWithStatusDTO(company, userCompanyStatus);
    }
}
