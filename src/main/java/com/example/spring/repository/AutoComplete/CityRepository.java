package com.example.spring.repository.AutoComplete;

import com.example.spring.model.AutoComplete.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    @Query("SELECT c FROM City c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<City> findByNameContainingIgnoreCase(String query);
}
