package com.example.spring.service.AutoComplete;

import com.example.spring.model.AutoComplete.IndustrySector;
import com.example.spring.repository.AutoComplete.IndustrySectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndustrySectorService {

    @Autowired
    private IndustrySectorRepository industrySectorRepository;

    public List<IndustrySector> searchIndustrySectorsByName(String query) {
        return industrySectorRepository.findByNameContainingIgnoreCase(query);
    }

    public List<IndustrySector> searchIndustrySectorsByNames(List<String> query) {
        return industrySectorRepository.findByNameIn(query);
    }

    public List<IndustrySector> searchIndustrySectorsByIds(List<Integer> query) {
        return industrySectorRepository.findByIdIn(query);
    }
}
