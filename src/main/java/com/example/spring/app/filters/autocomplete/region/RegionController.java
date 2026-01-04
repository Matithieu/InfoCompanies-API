package com.example.spring.app.filters.autocomplete.region;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/autocomplete/regions")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @PostMapping("/ids")
    public List<RegionModel> autocompleteRegionsByIds(@RequestBody List<Integer> query) {
        return regionService.searchRegionsByIds(query);
    }

    @PostMapping("/names")
    public List<RegionModel> autocompleteRegionsByNames(@RequestBody List<String> query) {
        return regionService.searchRegionsByNamesContainingIgnoreCase(query);
    }
}
