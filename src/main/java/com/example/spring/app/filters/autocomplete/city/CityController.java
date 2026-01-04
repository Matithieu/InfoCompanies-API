package com.example.spring.app.filters.autocomplete.city;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/autocomplete/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping("/ids")
    public List<CityModel> autocompleteCitiesByIds(@RequestBody List<Integer> query) {
        return cityService.searchCitiesByIds(query);
    }

    @PostMapping("/names")
    public List<CityModel> autocompleteCitiesByNames(@RequestBody List<String> query) {
        return cityService.searchCitiesByNameContainingIgnoreCaseAny(query);
    }
}
