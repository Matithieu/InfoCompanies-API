package com.example.spring.controller;

import com.example.spring.model.CompanySeen;
import com.example.spring.service.companySeen.CompanySeenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")

public class CompanySeenController {

    @Autowired
    private CompanySeenService companySeenService;

    @PutMapping("/update-company-ids/{companyId}")
    public void updateCompanyIds(@PathVariable Long companyId, @RequestBody List<Long> newCompanyIds) {
        companySeenService.updateCompanyIds(companyId, newCompanyIds);
    }

    @GetMapping("/companies-seen")
    public Optional<CompanySeen> getAllCompaniesSeen() {
        return companySeenService.getAllCompaniesSeen();
    }
}
