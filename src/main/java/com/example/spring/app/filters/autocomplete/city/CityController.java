package com.example.spring.app.filters.autocomplete.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/autocomplete")
public class CityController {

    @Autowired
    private CityService cityService;

    // Example: http://localhost:8080/api/v1/autocomplete/city?query=New
    @GetMapping("/city")
    public List<City> autocompleteCitiesByName(@RequestParam String query) {
        return cityService.searchCitiesByName(query);
    }

    @GetMapping("/city/ids")
    public List<City> autocompleteCitiesByIds(@RequestParam List<Integer> query) {
        return cityService.searchCitiesByIds(query);
    }

    @GetMapping("/cities")
    public List<City> autocompleteCitiesByNames(@RequestParam List<String> query) {
        return cityService.searchCitiesByNames(query);
    }
}
