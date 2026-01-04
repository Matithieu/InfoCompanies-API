package com.example.spring.app.filters.autocomplete.region;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

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
