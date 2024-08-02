package com.example.spring.controller;

import com.example.spring.model.AutoComplete.City;
import com.example.spring.model.AutoComplete.IndustrySector;
import com.example.spring.model.AutoComplete.LegalForm;
import com.example.spring.service.AutoComplete.CityService;
import com.example.spring.service.AutoComplete.IndustrySectorService;
import com.example.spring.service.AutoComplete.LegalFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/autocomplete")
public class AutoCompleteController {

    @Autowired
    private CityService cityService;

    @Autowired
    private IndustrySectorService industrySectorService;

    @Autowired
    private LegalFormService legalFormService;

    // Example: http://localhost:8080/api/v1/autocomplete/city?query=New
    @GetMapping("/city")
    public List<City> autocompleteCity(@RequestParam String query) {
        return cityService.searchCities(query);
    }

    // Example: http://localhost:8080/api/v1/autocomplete/industry-sector?query=New
    @GetMapping("/industry-sector")
    public List<IndustrySector> autocompleteIndustrySector(@RequestParam String query) {
        return industrySectorService.searchIndustrySector(query);
    }

    // Example: http://localhost:8080/api/v1/autocomplete/legal-form?query=New
    @GetMapping("/legal-form")
    public List<LegalForm> autocompleteLegalForm(@RequestParam String query) {
        return legalFormService.searchLegalForm(query);
    }
}
