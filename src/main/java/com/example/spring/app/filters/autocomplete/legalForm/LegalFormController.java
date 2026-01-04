package com.example.spring.app.filters.autocomplete.legalForm;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/autocomplete/legal-forms")
public class LegalFormController {

    private final LegalFormService legalFormService;

    public LegalFormController(LegalFormService legalFormService) {
        this.legalFormService = legalFormService;
    }

    @PostMapping("/ids")
    public List<LegalFormModel> autocompleteLegalFormsByIds(@RequestBody List<Integer> query) {
        return legalFormService.searchLegalFormsByIds(query);
    }

    @PostMapping("/names")
    public List<LegalFormModel> autocompleteLegalFormsByNames(@RequestBody List<String> query) {
        return legalFormService.searchLegalFormsByNamesContainingIgnoreCase(query);
    }
}
