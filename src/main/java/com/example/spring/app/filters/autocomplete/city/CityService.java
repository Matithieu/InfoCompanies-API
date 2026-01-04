package com.example.spring.app.filters.autocomplete.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public List<CityModel> searchCitiesByNameContainingIgnoreCaseAny(List<String> query) {
        if (query.size() > 1) {
            return cityRepository.findByNameIn(query);
        }

        return cityRepository.findByNameContainingIgnoreCase(query.getFirst());
    }

    public List<CityModel> searchCitiesByIds(List<Integer> query) {
        return cityRepository.findByIdIn(query);
    }
}
