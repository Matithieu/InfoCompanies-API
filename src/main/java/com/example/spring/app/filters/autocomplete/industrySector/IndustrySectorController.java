package com.example.spring.app.filters.autocomplete.industrySector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/autocomplete/industry-sectors")
public class IndustrySectorController {
    
    @Autowired
    private IndustrySectorService industrySectorService;

    @PostMapping("/ids")
    public List<IndustrySectorModel> autocompleteIndustrySectorsByIds(@RequestBody List<Integer> query) {
        return industrySectorService.searchIndustrySectorsByIds(query);
    }

    @PostMapping("/names")
    public List<IndustrySectorModel> autocompleteIndustrySectorsByNames(@RequestBody List<String> query) {
        return industrySectorService.searchIndustrySectorsByNameContainingIgnoreCase(query);
    }
}
