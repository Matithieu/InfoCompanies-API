package com.example.spring.controller;

import com.example.spring.DTO.CompanyDetails;
import com.example.spring.model.Company;
import com.example.spring.service.CompanyService;
import com.example.spring.utils.CompanyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.example.spring.utils.HeadersUtil.parseUserFromHeader;


@CrossOrigin
@RestController
@RequestMapping("/v1/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // Example: http://localhost:8080/api/v1/company/get-by-id/123
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable("id") Long id) {
        Company company = companyService.getCompanyById(id);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    // Example: http://localhost:8080/api/v1/company/get-by-ids?ids=1,2,3&page=0
    // For the To-Do
    @GetMapping("/get-by-ids")
    public ResponseEntity<Page<Company>> getCompaniesByIds(@RequestParam List<Long> ids,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Company> result = companyService.getCompaniesByAListOfIds(ids, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Example: http://localhost:8080/api/v1/company/get-seen-by-user?page=0
    @GetMapping("/get-seen-by-user")
    public Page<Company> getCompaniesSeenByUser(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        String userId = parseUserFromHeader();
        return companyService.getCompaniesSeenByUser(userId, pageable);
    }

    // Example: http://localhost:8080/api/v1/company/search-by-name?companyName=ExampleCompany&page=0
    @GetMapping("/search-by-name")
    public Page<CompanyDetails> searchCompaniesByName(@RequestParam("companyName") String companyName,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return companyService.searchCompanies(companyName, pageable);
    }

    // Example: http://localhost:8080/api/v1/company/filter-by-parameters?regions=region1,region2&cities=city1,city2&industrySectors=sector1,sector2&legalForms=form1,form2&page=0
    @GetMapping("/filter-by-parameters")
    public Page<Company> getCompaniesByFilters(
            @RequestParam(required = false) List<String> regions,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) List<String> industrySectors,
            @RequestParam(required = false) List<String> legalForms,
            @RequestParam(required = false) String comparator,
            @RequestParam(required = false) Integer numberOfEmployee,
            @RequestParam(required = false) List<String> socials,
            @RequestParam(required = false) List<String> contacts,
            @RequestParam(required = false) boolean isCompanySeen,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Cache and retrieve the total count
        long totalCompanies = companyService.countCompaniesByFilters(regions, cities, industrySectors, legalForms,
                comparator, numberOfEmployee, socials, contacts);

        String userId = parseUserFromHeader();
        Pageable pageable = PageRequest.of(page, size);
        Page<Company> companiesPage = companyService.findCompaniesByFilters(regions, cities, industrySectors, legalForms,
                comparator, numberOfEmployee, socials, contacts, isCompanySeen, userId, pageable);

        // Optionally set total elements manually (though Page already manages it)
        return new PageImpl<>(companiesPage.getContent(), pageable, totalCompanies);
    }

    // Example: http://localhost:8080/api/v1/company/random-unseen?page=0
    @GetMapping("/random-unseen")
    public ResponseEntity<Page<Company>> getRandomUnseenCompanies(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        String userId = parseUserFromHeader();
        Page<Company> result = companyService.findRandomUnseenCompanies(userId, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Make a request to the scrap API
    // Example: http://localhost:8080/api/v1/company/scrap?companyId=1
    @GetMapping("/scrap")
    public ResponseEntity<?> scrapCompany(@RequestParam Long companyId) {
        try {
            Company company = companyService.getCompanyById(companyId);

            // If the scrapping date is older than 1 day, then scrap the company again
            if ((company.getScrapingDate() == null) || (company.getScrapingDate().isBefore(LocalDate.now().minusDays(1)))) {
                Company companyScraped = companyService.scrapCompany(company);

                company.setPhoneNumber(companyScraped.getPhoneNumber());
                company.setWebsite(companyScraped.getWebsite());
                company.setInstagram(companyScraped.getInstagram());
                company.setFacebook(companyScraped.getFacebook());
                company.setTwitter(companyScraped.getTwitter());
                company.setLinkedin(companyScraped.getLinkedin());
                company.setYoutube(companyScraped.getYoutube());
                company.setEmail(companyScraped.getEmail());
                company.setScrapingDate(companyScraped.getScrapingDate());
                company.setReviews(companyScraped.getReviews());
                company.setSchedule(companyScraped.getSchedule());

                companyService.saveCompany(company);

                return new ResponseEntity<>(company, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("The company was scrapped less than 1 day ago", HttpStatus.TOO_EARLY);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to scrap company information: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Example: http://localhost:8080/api/v1/company/filter-by-parameters?regions=region1,region2&cities=city1,city2&industrySectors=sector1,sector2&legalForms=form1,form2&page=0
    @GetMapping("/landing-filter")
    public Page<Company> getCompaniesOnLandingByFilters(
            @RequestParam(required = false) List<String> regions,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) List<String> industrySectors,
            @RequestParam(required = false) List<String> legalForms,
            @RequestParam(required = false) String comparator,
            @RequestParam(required = false) Integer numberOfEmployee,
            @RequestParam(required = false) List<String> socials,
            @RequestParam(required = false) List<String> contacts
    ) {

        Pageable pageable = PageRequest.of(0, 10);
        // Cache and retrieve the total count
        long totalCompanies = companyService.countCompaniesByFilters(regions, cities, industrySectors, legalForms,
                comparator, numberOfEmployee, socials, contacts);

        String userId = "";
        Page<Company> companiesPage = companyService.findCompaniesByFilters(regions, cities, industrySectors, legalForms,
                comparator, numberOfEmployee, socials, contacts, false, userId, pageable);

        companiesPage = CompanyUtil.obstructCompanies(companiesPage);
        return new PageImpl<>(companiesPage.getContent(), pageable, totalCompanies);
    }
}
