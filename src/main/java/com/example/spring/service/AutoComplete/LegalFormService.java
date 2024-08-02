package com.example.spring.service.AutoComplete;

import com.example.spring.model.AutoComplete.LegalForm;
import com.example.spring.repository.AutoComplete.LegalFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegalFormService {

    @Autowired
    private LegalFormRepository legalFormRepository;

    public List<LegalForm> searchLegalForm(String query) {
        return legalFormRepository.findByNameContainingIgnoreCase(query);
    }
}
