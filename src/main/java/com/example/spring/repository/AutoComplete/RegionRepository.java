package com.example.spring.repository.AutoComplete;

import com.example.spring.model.AutoComplete.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query("SELECT c FROM Region c " +
            "WHERE LOWER(c.name) " +
            "LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY c.name " +
            "ASC LIMIT 25")
    List<Region> findByNameContainingIgnoreCase(String query);
}
