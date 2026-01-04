package com.example.spring.app.filters.autocomplete.industrySector;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndustrySectorService {

    private final IndustrySectorRepository industrySectorRepository;

    public IndustrySectorService(IndustrySectorRepository repository) {
        this.industrySectorRepository = repository;
    }

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
