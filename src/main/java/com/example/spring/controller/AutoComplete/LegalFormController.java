package com.example.spring.controller.AutoComplete;

import com.example.spring.model.AutoComplete.LegalForm;
import com.example.spring.service.AutoComplete.LegalFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/autocomplete")
public class LegalFormController {
    @Autowired
    private LegalFormService legalFormService;

    // Example: http://localhost:8080/api/v1/autocomplete/legalForm?query=New
    @GetMapping("/legalForm")
    public List<LegalForm> autocompleteLegalFormsByName(@RequestParam String query) {
        return legalFormService.searchLegalFormsByName(query);
    }

    @GetMapping("/legalForm/ids")
    public List<LegalForm> autocompleteLegalFormsByIds(@RequestParam List<Integer> query) {
        return legalFormService.searchLegalFormsByIds(query);
    }

    @GetMapping("/legalForms")
    public List<LegalForm> autocompleteLegalFormsByNames(@RequestParam List<String> query) {
        return legalFormService.searchLegalFormsByNames(query);
    }
}
