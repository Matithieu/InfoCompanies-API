package com.example.spring.repository.AutoComplete;

import com.example.spring.model.AutoComplete.IndustrySector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IndustrySectorRepository extends JpaRepository<IndustrySector, Long> {

    @Query("SELECT c FROM IndustrySector c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<IndustrySector> findByNameContainingIgnoreCase(String query);
}
