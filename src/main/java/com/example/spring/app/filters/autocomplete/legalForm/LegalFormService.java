package com.example.spring.app.filters.autocomplete.legalForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegalFormService {

    @Autowired
    private LegalFormRepository legalFormRepository;

    public List<LegalForm> searchLegalFormsByName(String query) {
        return legalFormRepository.findByNameContainingIgnoreCase(query);
    }

    public List<LegalForm> searchLegalFormsByNames(List<String> query) {
        return legalFormRepository.findByNameIn(query);
    }

    public List<LegalForm> searchLegalFormsByIds(List<Integer> query) {
        return legalFormRepository.findByIdIn(query);
    }
}
