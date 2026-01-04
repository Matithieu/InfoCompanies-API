package com.example.spring.app.filters.autocomplete.legalForm;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegalFormService {

    private final LegalFormRepository legalFormRepository;

    public LegalFormService(LegalFormRepository legalFormRepository) {
        this.legalFormRepository = legalFormRepository;
    }

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
