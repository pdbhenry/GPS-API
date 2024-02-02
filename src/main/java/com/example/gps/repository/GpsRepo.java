package com.example.gps.repository;

import com.example.gps.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GpsRepo extends JpaRepository<Location,Long> {
    List<Location> findByNameContaining(String name);
}
