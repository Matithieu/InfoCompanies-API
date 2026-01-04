package com.example.spring.app.filters.autocomplete.industrySector;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/autocomplete/industry-sectors")
public class IndustrySectorController {
    
    private final IndustrySectorService industrySectorService;

    public IndustrySectorController(IndustrySectorService industrySectorService) {
        this.industrySectorService = industrySectorService;
    }

    @PostMapping("/ids")
    public List<IndustrySectorModel> autocompleteIndustrySectorsByIds(@RequestBody List<Integer> query) {
        return industrySectorService.searchIndustrySectorsByIds(query);
    }

    @PostMapping("/names")
    public List<IndustrySectorModel> autocompleteIndustrySectorsByNames(@RequestBody List<String> query) {
        return industrySectorService.searchIndustrySectorsByNameContainingIgnoreCase(query);
    }
}
