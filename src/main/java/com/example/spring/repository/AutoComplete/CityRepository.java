package com.example.spring.repository.AutoComplete;

import com.example.spring.model.AutoComplete.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer> {

    @Query("SELECT c FROM City c " +
            "WHERE LOWER(c.name) " +
            "LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY c.name " +
            "ASC LIMIT 25")
    List<City> findByNameContainingIgnoreCase(String query);

    List<City> findByNameIn(List<String> names);

    List<City> findByIdIn(List<Integer> ids);
}
