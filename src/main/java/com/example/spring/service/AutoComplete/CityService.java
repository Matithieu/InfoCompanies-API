package com.example.spring.service.AutoComplete;

import com.example.spring.model.AutoComplete.City;
import com.example.spring.repository.AutoComplete.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public List<City> searchCities(String query) {
        return cityRepository.findByNameContainingIgnoreCase(query);
    }
}
