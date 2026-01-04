package com.example.spring.app.filters.autocomplete.legalForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegalFormService {

    @Autowired
    private LegalFormRepository legalFormRepository;

    public List<LegalFormModel> searchLegalFormsByNamesContainingIgnoreCase(List<String> query) {
        if (query.size() > 1) {
            return legalFormRepository.findByNameIn(query);
        }

        return legalFormRepository.findByNameContainingIgnoreCase(query.getFirst());
    }

    public List<LegalFormModel> searchLegalFormsByIds(List<Integer> query) {
        return legalFormRepository.findByIdIn(query);
    }
}
