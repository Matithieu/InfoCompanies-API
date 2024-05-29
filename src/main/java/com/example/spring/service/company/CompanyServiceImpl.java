package com.example.spring.service.company;

import com.example.spring.model.Company;
import com.example.spring.model.CompanyDetails;
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

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findCompanyById(id);
    }

    @Override
    public Company getCompanyByCompanyName(String companyName) {
        return companyRepository.findByCompanyName(companyName);
    }

    @Override
    public void saveCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public void deleteCompanyById(Long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public List<Company> getAmountOfCompanies(int amount) {
        return companyRepository.findAll().subList(0, amount);
    }

    @Override
    public Page<CompanyDetails> searchCompanies(String companyName, Pageable pageable) {
        return companyRepository.findCompanyDetailsByCompanyName(companyName, pageable);
    }

    @Override
    public Page<Company> getCompaniesByIndustrySectorAndRegion(String industrySector, String region, Pageable pageable) {
        return companyRepository.findByIndustrySectorContainingAndRegionContaining(industrySector, region, pageable);
    }

    @Override
    public Page<Company> findRandomCompanies(Pageable pageable) {
        return companyRepository.findRandomCompanies(pageable);
    }

    @Override
    public Page<Company> getCompaniesByAListOfIds(List<Long> ids, Pageable pageable) {
        return companyRepository.findAllByIdIn(ids, pageable);
    }

    // Make a request to the scrap API
    @Override
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
            company.setCompanyName(jsonNode.get("companyName").asText());
            company.setPhoneNumber(jsonNode.get("phoneNumber").asText());
            company.setWebsite(jsonNode.get("website").asText());
            company.setInstagram(jsonNode.get("instagram").asText(""));
            company.setFacebook(jsonNode.get("facebook").asText(""));
            company.setTwitter(jsonNode.get("twitter").asText(""));
            company.setLinkedin(jsonNode.get("linkedin").asText(""));
            company.setYoutube(jsonNode.get("youtube").asText(""));
            company.setEmail(jsonNode.get("email").asText(""));
            company.setScrapingDate(LocalDate.parse(jsonNode.get("scrapingDate").asText()));
            company.setReviews(jsonNode.get("reviews").asText());
            company.setSchedule(jsonNode.get("schedule").asText());

            return company;

        } catch (Exception e) {
            throw new RuntimeException("Failed to scrap company information: ", e);
        }
    }
}