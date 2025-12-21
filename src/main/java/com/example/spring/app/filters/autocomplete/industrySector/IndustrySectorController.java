package com.example.spring.app.filters.autocomplete.industrySector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/autocomplete")
public class IndustrySectorController {
    
    @Autowired
    private IndustrySectorService industrySectorService;

    // Example: http://localhost:8080/api/v1/autocomplete/industrySector?query=New
    @GetMapping("/industrySector")
    public List<IndustrySector> autocompleteIndustrySectorsByName(@RequestParam String query) {
        return industrySectorService.searchIndustrySectorsByName(query);
    }

    @GetMapping("/industrySector/ids")
    public List<IndustrySector> autocompleteIndustrySectorsByIds(@RequestParam List<Integer> query) {
        return industrySectorService.searchIndustrySectorsByIds(query);
    }

    @GetMapping("/industrySectors")
    public List<IndustrySector> autocompleteIndustrySectorsByNames(@RequestParam List<String> query) {
        return industrySectorService.searchIndustrySectorsByNames(query);
    }
}
