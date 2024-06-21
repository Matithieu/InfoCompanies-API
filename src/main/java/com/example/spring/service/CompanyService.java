package com.example.spring.service;

import com.example.spring.model.Company;
import com.example.spring.DTO.CompanyDetails;
import com.example.spring.repository.CompanyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.function.Consumer;

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

    public void saveCompany(Company company) {
        companyRepository.save(company);
    }

    public Page<CompanyDetails> searchCompanies(String companyName, Pageable pageable) {
        return companyRepository.findCompanyDetailsByCompanyName(companyName, pageable);
    }

    public Page<Company> getCompaniesByIndustrySectorAndRegion(String industrySector, String region, Pageable pageable) {
        return companyRepository.findByIndustrySectorContainingAndRegionContaining(industrySector, region, pageable);
    }

    public Page<Company> findRandomCompanies(Pageable pageable) {
        return companyRepository.findRandomCompanies(pageable);
    }

    public Page<Company> findRandomUnseenCompanies(String userId, Pageable pageable) {
        return companyRepository.findRandomSeenCompanies(userId, pageable);
    }

    private void setIfNotEmpty(JsonNode jsonNode, String fieldName, Consumer<String> setter) {
        if (jsonNode.hasNonNull(fieldName) && !jsonNode.get(fieldName).asText().isEmpty()) {
            setter.accept(jsonNode.get(fieldName).asText());
        }
    }

    // Make a request to the scrap API
    public Company scrapCompany(String companyName, String address) {
        try {
            HttpResponse<String> response;
            try (HttpClient client = HttpClient.newHttpClient()) {

                // Create the request
                String encodedCompanyName = URLEncoder.encode(companyName, StandardCharsets.UTF_8);
                String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);

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

            Company company = new Company();

            setIfNotEmpty(jsonNode, "companyName", company::setCompanyName);
            setIfNotEmpty(jsonNode, "phoneNumber", company::setPhoneNumber);
            setIfNotEmpty(jsonNode, "website", company::setWebsite);
            setIfNotEmpty(jsonNode, "instagram", company::setInstagram);
            setIfNotEmpty(jsonNode, "facebook", company::setFacebook);
            setIfNotEmpty(jsonNode, "twitter", company::setTwitter);
            setIfNotEmpty(jsonNode, "linkedin", company::setLinkedin);
            setIfNotEmpty(jsonNode, "youtube", company::setYoutube);
            setIfNotEmpty(jsonNode, "email", company::setEmail);
            setIfNotEmpty(jsonNode, "scrapingDate", (value) -> company.setScrapingDate(LocalDate.parse(value)));
            setIfNotEmpty(jsonNode, "reviews", company::setReviews);
            setIfNotEmpty(jsonNode, "schedule", company::setSchedule);

            return company;

        } catch (Exception e) {
            throw new RuntimeException("Failed to scrap company information: ", e);
        }
    }
}