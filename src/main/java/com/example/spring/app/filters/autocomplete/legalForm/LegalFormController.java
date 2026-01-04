package com.example.spring.app.filters.autocomplete.legalForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/autocomplete/legal-forms")
public class LegalFormController {
    @Autowired
    private LegalFormService legalFormService;

    @PostMapping("/ids")
    public List<LegalFormModel> autocompleteLegalFormsByIds(@RequestBody List<Integer> query) {
        return legalFormService.searchLegalFormsByIds(query);
    }

    @PostMapping("/names")
    public List<LegalFormModel> autocompleteLegalFormsByNames(@RequestBody List<String> query) {
        return legalFormService.searchLegalFormsByNamesContainingIgnoreCase(query);
    }
}
