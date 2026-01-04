package com.example.spring.app.filters.autocomplete.region;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/autocomplete/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @PostMapping("/ids")
    public List<RegionModel> autocompleteRegionsByIds(@RequestBody List<Integer> query) {
        return regionService.searchRegionsByIds(query);
    }

    @PostMapping("/names")
    public List<RegionModel> autocompleteRegionsByNames(@RequestBody List<String> query) {
        return regionService.searchRegionsByNamesContainingIgnoreCase(query);
    }
}
