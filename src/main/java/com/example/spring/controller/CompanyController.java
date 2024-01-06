package com.example.spring.controller;

import com.example.spring.model.Company;
import com.example.spring.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")

public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // http://127.0.0.1:8080/api/v1/search?name=Maison&page=0
    @GetMapping("/search")
    public Page<Company> searchCompanies(@RequestParam String name, int page) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        return companyService.searchCompanies(name, pageable);
    }
}
