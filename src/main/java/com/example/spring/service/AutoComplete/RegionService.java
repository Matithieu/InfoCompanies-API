package com.example.spring.service.AutoComplete;

import com.example.spring.model.AutoComplete.Region;
import com.example.spring.repository.AutoComplete.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public List<Region> searchRegionsByName(String query) {
        return regionRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Region> searchRegionsByNames(List<String> query) {
        return regionRepository.findByNameIn(query);
    }

    public List<Region> searchRegionsByIds(List<Integer> query) {
        return regionRepository.findByIdIn(query);
    }
}
