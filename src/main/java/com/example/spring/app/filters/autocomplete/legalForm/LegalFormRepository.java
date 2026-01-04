package com.example.spring.app.filters.autocomplete.legalForm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LegalFormRepository extends JpaRepository<LegalFormModel, Integer> {

    @Query("SELECT l FROM LegalFormModel l " +
            "WHERE LOWER(l.name) " +
            "LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY l.name " +
            "ASC LIMIT 25")
    List<LegalFormModel> findByNameContainingIgnoreCase(String query);

    List<LegalFormModel> findByNameIn(List<String> names);

    List<LegalFormModel> findByIdIn(List<Integer> ids);
}