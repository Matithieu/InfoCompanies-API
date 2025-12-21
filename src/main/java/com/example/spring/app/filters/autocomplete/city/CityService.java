package com.example.spring.app.filters.autocomplete.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public List<City> searchCitiesByName(String query) {
        return cityRepository.findByNameContainingIgnoreCase(query);
    }

    public List<City> searchCitiesByNames(List<String> query) {
        return cityRepository.findByNameIn(query);
    }

    public List<City> searchCitiesByIds(List<Integer> query) {
        return cityRepository.findByIdIn(query);
    }
}
