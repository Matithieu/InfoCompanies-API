package com.example.spring.app.filters.autocomplete.city;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<CityModel, Integer> {

    @Query("SELECT c FROM CityModel c " +
            "WHERE LOWER(c.name) " +
            "LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY c.name " +
            "ASC LIMIT 25")
    List<CityModel> findByNameContainingIgnoreCase(String query);

    List<CityModel> findByNameIn(List<String> names);

    List<CityModel> findByIdIn(List<Integer> ids);
}
