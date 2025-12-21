package com.example.spring.app.filters.autocomplete.industrySector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IndustrySectorRepository extends JpaRepository<IndustrySector, Integer> {

    @Query("SELECT c FROM IndustrySector c " +
            "WHERE LOWER(c.name) " +
            "LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY c.name " +
            "ASC LIMIT 20")
    List<IndustrySector> findByNameContainingIgnoreCase(String query);

    List<IndustrySector> findByNameIn(List<String> names);

    List<IndustrySector> findByIdIn(List<Integer> ids);
}
