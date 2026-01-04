package com.example.spring.app.filters.autocomplete.region;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public List<RegionModel> searchRegionsByNamesContainingIgnoreCase(List<String> query) {
        if (query.size() > 1) {
            return regionRepository.findByNameIn(query);
        }

        return regionRepository.findByNameContainingIgnoreCase(query.getFirst());
    }

    public List<RegionModel> searchRegionsByIds(List<Integer> query) {
        return regionRepository.findByIdIn(query);
    }
}
