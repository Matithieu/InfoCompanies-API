package com.example.spring.controller;

import com.example.spring.model.CompanySeen;
import com.example.spring.service.CompanySeenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.spring.security.utils.SecurityUtils.parseUserFromHeader;

@CrossOrigin
@RestController
@RequestMapping("/v1/company-seen")
public class CompanySeenController {

    @Autowired
    private CompanySeenService companySeenService;

    @PostMapping("/update-company-ids")
    public void updateCompanyIds(@RequestBody List<Long> companyIds) {
        String userId = parseUserFromHeader();
        companySeenService.updateCompanySeen(userId, companyIds);
    }

    @GetMapping("/companies-seen")
    public Optional<CompanySeen> getAllCompaniesSeen() {
        String userId = parseUserFromHeader();
        return companySeenService.getAllCompaniesSeen(userId);
    }
}
