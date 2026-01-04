package com.example.spring.app.filters.autocomplete.industrySector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndustrySectorService {

    @Autowired
    private IndustrySectorRepository industrySectorRepository;

    public List<IndustrySectorModel> searchIndustrySectorsByNameContainingIgnoreCase(List<String> query) {
        if (query.size() > 1) {
            return industrySectorRepository.findByNameIn(query);
        }

        return industrySectorRepository.findByNameContainingIgnoreCase(query.getFirst());
    }

    public List<IndustrySectorModel> searchIndustrySectorsByIds(List<Integer> query) {
        return industrySectorRepository.findByIdIn(query);
    }
}
