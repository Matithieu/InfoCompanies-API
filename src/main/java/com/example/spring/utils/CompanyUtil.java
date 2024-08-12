package com.example.spring.utils;

import com.example.spring.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
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
}
