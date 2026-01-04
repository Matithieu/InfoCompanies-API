package com.example.spring.app.filters.autocomplete.region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegionRepository extends JpaRepository<RegionModel, Integer> {

    @Query("SELECT r FROM RegionModel r " +
            "WHERE LOWER(r.name) " +
            "LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY r.name " +
            "ASC LIMIT 25")
    List<RegionModel> findByNameContainingIgnoreCase(String query);

    List<RegionModel> findByNameIn(List<String> names);

    List<RegionModel> findByIdIn(List<Integer> ids);
}
