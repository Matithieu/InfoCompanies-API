package com.example.spring.app.company;

import com.example.spring.app.company.dto.CompanyDetails;
import com.example.spring.app.company.dto.NumberOfEmployeeFilterDTO;
import com.example.spring.app.company.objects.ContactDTO;
import com.example.spring.app.company.objects.SocialMediaDTO;
import com.example.spring.common.utils.LogUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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
import java.util.Map;

import static com.example.spring.app.company.CompanyUtil.setIfNotEmpty;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyModel getCompanyById(Integer id) {
        return companyRepository.findCompanyById(id);
    }

    public Page<CompanyModel> getCompaniesSeenByUser(String userId, Pageable pageable) {
        return companyRepository.findCompaniesSeenByUser(userId, pageable);
    }

    @Cacheable(value = "companySearch", key = "#companyName + #pageable")
    public Page<CompanyDetails> searchCompanies(String companyName, Pageable pageable) {
        return companyRepository.findCompanyDetailsByCompanyName(companyName, pageable);
    }

    public Page<CompanyModel> findCompaniesByFilters(List<String> regionNames, List<String> cityNames, List<String> industrySectorNames, List<String> legalFormNames,
                                                     NumberOfEmployeeFilterDTO numberOfEmployeeFilter, SocialMediaDTO socials, ContactDTO contacts, Boolean isCompanySeen,
                                                     String userId, Pageable pageable) {

        Specification<CompanyModel> specification = Specification.where(CompanySpecification.regionsIn(regionNames))
                .and(CompanySpecification.citiesIn(cityNames))
                .and(CompanySpecification.industrySectorsIn(industrySectorNames))
                .and(CompanySpecification.legalFormsIn(legalFormNames))
                .and(CompanySpecification.employeeComparator(numberOfEmployeeFilter))
                .and(CompanySpecification.socialMediaNotNull(socials))
                .and(CompanySpecification.contactInfoNotNull(contacts))
                .and(CompanySpecification.notSeenByUser(isCompanySeen, userId));

        return companyRepository.findAll(specification, pageable);
    }

    @Cacheable(value = "companyCounts", key = "#root.methodName + #regionNames + #cityNames + #industrySectorNames + #legalFormNames + #numberOfEmployeeFilter + #socials + #contacts")
    public long countCompaniesByFilters(List<String> regionNames, List<String> cityNames, List<String> industrySectorNames, List<String> legalFormNames,
                                        NumberOfEmployeeFilterDTO numberOfEmployeeFilter, SocialMediaDTO socials, ContactDTO contacts) {

        Specification<CompanyModel> specification = Specification.where(CompanySpecification.regionsIn(regionNames))
                .and(CompanySpecification.citiesIn(cityNames))
                .and(CompanySpecification.industrySectorsIn(industrySectorNames))
                .and(CompanySpecification.legalFormsIn(legalFormNames))
                .and(CompanySpecification.employeeComparator(numberOfEmployeeFilter))
                .and(CompanySpecification.socialMediaNotNull(socials))
                .and(CompanySpecification.contactInfoNotNull(contacts));

        // Run a custom count query that only calculates the total number of companies matching the filters
        return companyRepository.count(specification);
    }

    public Page<CompanyModel> findRandomUnseenCompanies(String userId, Pageable pageable) {
        return companyRepository.findRandomUnseenCompanies(userId, pageable);
    }

    @RateLimiter(name = "scrapService")
    public CompanyModel scrapCompany(CompanyModel company) {
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

            CompanyModel companyScrapped = new CompanyModel();

            // TODO: Verify what's ifNotEmpty means. Currently it still updates the values even if they are empty in the response
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
            setIfNotEmpty(jsonNode, "reviews", (value) -> companyScrapped.setReviews(new ObjectMapper().convertValue(jsonNode.get("reviews"), new TypeReference<Map<String, Object>>() {})));
            setIfNotEmpty(jsonNode, "schedule", companyScrapped::setSchedule);

            return companyScrapped;

        } catch (Exception e) {
            LogUtil.error("Failed to scrap company information: ", e);
            throw new RuntimeException("Failed to scrap company information: ", e);
        }
    }

    @CacheEvict(value = "companyCounts", allEntries = true)
    public void saveCompany(CompanyModel company) {
        companyRepository.save(company);
    }

    @CacheEvict(value = "companyCounts", allEntries = true)
    public void deleteCompany(Integer id) {
        companyRepository.deleteById(id);
    }
}