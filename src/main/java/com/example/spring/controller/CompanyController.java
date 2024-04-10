package com.example.spring.controller;

import com.example.spring.model.Company;
import com.example.spring.model.CompanyDetails;
import com.example.spring.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // http://127.0.0.1:8080/api/v1/company/search?name=Maison&page=0
    @GetMapping("/search")
    public Page<CompanyDetails> searchCompanies(@RequestParam String name, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").ascending());
        return companyService.searchCompanies(name, pageable);
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('verified')")
    public String test() {
        return "Test";
    }

    // http://127.0.0.1:8080/api/v1/company/recherche?secteurActivite=&region=Bretagne&page=0
    @GetMapping("/companies")
    public ResponseEntity<Page<Company>> getCompanyByParams(@RequestParam String secteurActivite, @RequestParam String region, Integer page) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        Page<Company> result = companyService.getCompaniesBySecteurActiviteAndRegion(secteurActivite, region, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // http://127.0.0.1:8080/api/v1/company/random-companies?page=0
   @GetMapping("/random-companies")
    public ResponseEntity<Page<Company>> getRandomCompanies(@RequestParam Integer page) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        Page<Company> result = companyService.findRandomCompanies(pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // http://127.0.0.1:8080/api/v1/company/1
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @GetMapping("/companies-by-ids")
    public ResponseEntity<Page<Company>> getCompaniesByAListOfIds(@RequestParam List<Long> ids, Integer page) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        Page<Company> result = companyService.getCompaniesByAListOfIds(ids, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
