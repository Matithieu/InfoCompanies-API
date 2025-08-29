package com.example.spring.utils;

import com.example.spring.dto.CompanyDtoWithStatusDTO;
import com.example.spring.dto.company.CompanyDTO;
import com.example.spring.model.Company;
import com.example.spring.model.UserCompanyStatus;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CompanyUtil {
    public static String maskData(String data) {
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

    public static Page<CompanyDtoWithStatusDTO> fillPaginationCompanyDtoWithStatusDto(Page<Company> companiesPage, List<UserCompanyStatus> userCompanyStatuses) {
        // Fill the CompanyWithStatusDTO with the company and its status
        Page<CompanyDtoWithStatusDTO> companyDTOWithStatusDTOS;

        if (userCompanyStatuses.isEmpty()) {
            companyDTOWithStatusDTOS = new PageImpl<>(companiesPage.getContent().stream()
                    .map(company -> new CompanyDtoWithStatusDTO(company.toCompanyDTO(), null))
                    .collect(Collectors.toList()), companiesPage.getPageable(), companiesPage.getTotalElements());
        } else {
            companyDTOWithStatusDTOS = new PageImpl<>(companiesPage.getContent().stream()
                    .map(company -> new CompanyDtoWithStatusDTO(company.toCompanyDTO(), userCompanyStatuses.stream()
                            .filter(userCompanyStatus -> userCompanyStatus.getCompanyId().equals(company.getId()))
                            .findFirst()
                            .orElse(null)))
                    .collect(Collectors.toList()), companiesPage.getPageable(), companiesPage.getTotalElements());
        }

        return companyDTOWithStatusDTOS;
    }

    public static CompanyDtoWithStatusDTO fillCompanyDtoWithStatusDto(CompanyDTO companyDTO, UserCompanyStatus userCompanyStatus) {
        return new CompanyDtoWithStatusDTO(companyDTO, userCompanyStatus);
    }
}
