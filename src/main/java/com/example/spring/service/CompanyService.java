package com.example.spring.service;

import com.example.spring.DTO.CompanyDetails;
import com.example.spring.model.Company;
import com.example.spring.repository.CompanyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Company> getCompaniesByAListOfIds(List<Long> ids, Pageable pageable) {
        return companyRepository.findAllByIdIn(ids, pageable);
    }

    @Cacheable(value = "companySearch", key = "#companyName + #pageable")
    public Page<CompanyDetails> searchCompanies(String companyName, Pageable pageable) {
        return companyRepository.findCompanyDetailsByCompanyName(companyName, pageable);
    }

    public Page<Company> getCompaniesByFilters(List<String> regions,
                                               List<String> cities,
                                               List<String> industrySectors,
                                               List<String> legalForms,
                                               String comparator,
                                               Integer numberOfEmployee,
                                               List<String> socials,
                                               Pageable pageable) {

        boolean includeLinkedin = false;
        boolean includeYoutube = false;
        boolean includeFacebook = false;
        boolean includeInstagram = false;
        boolean includeTwitter = false;

        // Check if the socials list is not null and not empty
        if (socials != null && !socials.isEmpty()) {
            includeLinkedin = socials.contains("linkedin");
            includeYoutube = socials.contains("youtube");
            includeFacebook = socials.contains("facebook");
            includeInstagram = socials.contains("instagram");
            includeTwitter = socials.contains("twitter");
        }

        if (numberOfEmployee == null || comparator == null ||
                (!comparator.equals(">") && !comparator.equals("<") && !comparator.equals("="))) {
            return companyRepository.findCompaniesByFilters(
                    regions, cities, industrySectors, legalForms,
                    includeLinkedin, includeYoutube, includeFacebook,
                    includeInstagram, includeTwitter, pageable);
        }

        // Apply the filtering by comparator and numberOfEmployee if valid
        return companyRepository.findCompaniesByFiltersWithEmployeeFilter(
                regions, cities, industrySectors, legalForms, comparator,
                numberOfEmployee, includeLinkedin, includeYoutube,
                includeFacebook, includeInstagram, includeTwitter, pageable);
    }

    public Page<Company> findRandomCompanies(Pageable pageable) {
        return companyRepository.findRandomCompanies(pageable);
    }

    public Page<Company> findRandomUnseenCompanies(String userId, Pageable pageable) {
        return companyRepository.findRandomSeenCompanies(userId, pageable);
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

    public void saveCompany(Company company) {
        companyRepository.save(company);
    }
}