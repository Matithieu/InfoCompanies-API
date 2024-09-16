package com.example.spring.repository.AutoComplete;

import com.example.spring.model.AutoComplete.LegalForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LegalFormRepository extends JpaRepository<LegalForm, Long> {

    @Query("SELECT c FROM LegalForm c " +
            "WHERE LOWER(c.name) " +
            "LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY c.name " +
            "ASC LIMIT 20")
    List<LegalForm> findByNameContainingIgnoreCase(String query);
}