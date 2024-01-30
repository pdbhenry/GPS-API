package com.example.gps.repository;

import com.example.gps.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GpsRepo extends JpaRepository<Location,Long> {
}
