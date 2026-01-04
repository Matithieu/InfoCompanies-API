package com.example.spring.app.filters.autocomplete.industrySector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IndustrySectorRepository extends JpaRepository<IndustrySectorModel, Integer> {

    @Query("SELECT i FROM IndustrySectorModel i " +
            "WHERE LOWER(i.name) " +
            "LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY i.name " +
            "ASC LIMIT 25")
    List<IndustrySectorModel> findByNameContainingIgnoreCase(String query);

    List<IndustrySectorModel> findByNameIn(List<String> names);

    List<IndustrySectorModel> findByIdIn(List<Integer> ids);
}
