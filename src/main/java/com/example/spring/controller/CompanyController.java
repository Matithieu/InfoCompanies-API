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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/v1/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // Example: http://localhost:8080/api/v1/company/search-by-name?companyName=ExampleCompany&page=0
    @GetMapping("/search-by-name")
    public Page<CompanyDetails> searchCompaniesByName(@RequestParam("companyName") String companyName,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return companyService.searchCompanies(companyName, pageable);
    }

    // Example: http://localhost:8080/api/v1/company/get-by-id/123
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable("id") Long id) {
        Company company = companyService.getCompanyById(id);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    // Example: http://localhost:8080/api/v1/company/filter-by-parameters?sector=Technology&region=California&page=0
    @GetMapping("/filter-by-parameters")
    public ResponseEntity<Page<Company>> getCompaniesByParameters(@RequestParam(value = "sector", required = false) String sector,
                                                                  @RequestParam(value = "region", required = false) String region,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Company> result = companyService.getCompaniesByIndustrySectorAndRegion(sector, region, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Example: http://localhost:8080/api/v1/company/random?page=0
    @GetMapping("/random")
    public ResponseEntity<Page<Company>> getRandomCompanies(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Company> result = companyService.findRandomCompanies(pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Example: http://localhost:8080/api/v1/company/get-by-ids?ids=1,2,3&page=0
    @GetMapping("/get-by-ids")
    public ResponseEntity<Page<Company>> getCompaniesByIds(@RequestParam List<Long> ids,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Company> result = companyService.getCompaniesByAListOfIds(ids, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
