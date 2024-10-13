package com.example.spring.service;

import com.example.spring.DTO.CompanyDetails;
import com.example.spring.model.Company;
import com.example.spring.repository.CompanyRepository;
import com.example.spring.specification.CompanySpecification;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static com.example.spring.utils.CompanyUtil.setIfNotEmpty;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Company getCompanyById(Long id) {
        return companyRepository.findCompanyById(id);
    }

    public Page<Company> getCompaniesSeenByUser(String userId, Pageable pageable) {
        return companyRepository.findCompaniesSeenByUser(userId, pageable);
    }

    @Cacheable(value = "companySearch", key = "#companyName + #pageable")
    public Page<CompanyDetails> searchCompanies(String companyName, Pageable pageable) {
        return companyRepository.findCompanyDetailsByCompanyName(companyName, pageable);
    }

    public Page<Company> findCompaniesByFilters(List<String> regions, List<String> cities, List<String> industrySectors, List<String> legalForms,
                                                String comparator, Integer numberOfEmployee, List<String> socials, List<String> contacts, boolean isCompanySeen,
                                                String userId, Pageable pageable) {

        Specification<Company> specification = Specification.where(CompanySpecification.regionIn(regions))
                .and(CompanySpecification.cityIn(cities))
                .and(CompanySpecification.industrySectorIn(industrySectors))
                .and(CompanySpecification.legalFormIn(legalForms))
                .and(CompanySpecification.employeeComparator(comparator, numberOfEmployee))
                .and(CompanySpecification.socialMediaNotNull(socials))
                .and(CompanySpecification.contactInfoNotNull(contacts))
                .and(CompanySpecification.notSeenByUser(isCompanySeen, userId));

        return companyRepository.findAll(specification, pageable);
    }

    @Cacheable(value = "companyCounts", key = "#root.methodName + #regions + #cities + #industrySectors + #legalForms + #comparator + #numberOfEmployee + #socials + #contacts")
    public long countCompaniesByFilters(List<String> regions, List<String> cities, List<String> industrySectors, List<String> legalForms,
                                        String comparator, Integer numberOfEmployee, List<String> socials, List<String> contacts) {

        Specification<Company> specification = Specification.where(CompanySpecification.regionIn(regions))
                .and(CompanySpecification.cityIn(cities))
                .and(CompanySpecification.industrySectorIn(industrySectors))
                .and(CompanySpecification.legalFormIn(legalForms))
                .and(CompanySpecification.employeeComparator(comparator, numberOfEmployee))
                .and(CompanySpecification.socialMediaNotNull(socials))
                .and(CompanySpecification.contactInfoNotNull(contacts));

        // Run a custom count query that only calculates the total number of companies matching the filters
        return companyRepository.count(specification);
    }

    public Page<Company> findRandomUnseenCompanies(String userId, Pageable pageable) {
        return companyRepository.findRandomUnseenCompanies(userId, pageable);
    }

    public Company scrapCompany(Company company) {
        try {
            HttpResponse<String> response;
            try (HttpClient client = HttpClient.newHttpClient()) {

                // Create the request
                String encodedCompanyName = URLEncoder.encode(company.getCompanyName(), StandardCharsets.UTF_8);
                String encodedAddress = URLEncoder.encode(company.getAddress(), StandardCharsets.UTF_8);

                String url = String.format("http://scraping:8081/api/company-info?companyName=%s&address=%s", encodedCompanyName, encodedAddress);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(url))
                        .GET()
                        .build();

                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            }

            // Parse the response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());

            Company companyScrapped = new Company();

            setIfNotEmpty(jsonNode, "companyName", companyScrapped::setCompanyName);
            setIfNotEmpty(jsonNode, "phoneNumber", companyScrapped::setPhoneNumber);
            setIfNotEmpty(jsonNode, "website", companyScrapped::setWebsite);
            setIfNotEmpty(jsonNode, "instagram", companyScrapped::setInstagram);
            setIfNotEmpty(jsonNode, "facebook", companyScrapped::setFacebook);
            setIfNotEmpty(jsonNode, "twitter", companyScrapped::setTwitter);
            setIfNotEmpty(jsonNode, "linkedin", companyScrapped::setLinkedin);
            setIfNotEmpty(jsonNode, "youtube", companyScrapped::setYoutube);
            setIfNotEmpty(jsonNode, "email", companyScrapped::setEmail);
            setIfNotEmpty(jsonNode, "scrapingDate", (value) -> companyScrapped.setScrapingDate(LocalDate.parse(value)));
            setIfNotEmpty(jsonNode, "reviews", companyScrapped::setReviews);
            setIfNotEmpty(jsonNode, "schedule", companyScrapped::setSchedule);

            return companyScrapped;

        } catch (Exception e) {
            throw new RuntimeException("Failed to scrap company information: ", e);
        }
    }

    @CacheEvict(value = "companyCounts", allEntries = true)
    public void saveCompany(Company company) {
        companyRepository.save(company);
    }

    @CacheEvict(value = "companyCounts", allEntries = true)
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}