package com.example.spring.app.filters.autocomplete.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/autocomplete/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping("/ids")
    public List<CityModel> autocompleteCitiesByIds(@RequestBody List<Integer> query) {
        return cityService.searchCitiesByIds(query);
    }

    @PostMapping("/names")
    public List<CityModel> autocompleteCitiesByNames(@RequestBody List<String> query) {
        return cityService.searchCitiesByNameContainingIgnoreCaseAny(query);
    }
}
