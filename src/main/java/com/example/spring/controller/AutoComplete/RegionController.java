package com.example.spring.controller.AutoComplete;

import com.example.spring.model.AutoComplete.Region;
import com.example.spring.service.AutoComplete.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/autocomplete")
public class RegionController {

    @Autowired
    private RegionService regionService;

    // Example: http://localhost:8080/api/v1/autocomplete/region?query=New
    @GetMapping("/region")
    public List<Region> autocompleteRegionsByName(@RequestParam String query) {
        return regionService.searchRegionsByName(query);
    }

    @GetMapping("/region/ids")
    public List<Region> autocompleteRegionsByIds(@RequestParam List<Integer> query) {
        return regionService.searchRegionsByIds(query);
    }

    @GetMapping("/regions")
    public List<Region> autocompleteRegionsByNames(@RequestParam List<String> query) {
        return regionService.searchRegionsByNames(query);
    }
}
