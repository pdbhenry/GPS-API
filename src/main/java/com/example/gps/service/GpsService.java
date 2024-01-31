package com.example.gps.service;

import com.example.gps.model.Location;

import java.util.List;

public interface GpsService {
    List<Location> findAllLocations();
    Location saveLocation(Location location);
    Location findLocationById(Long locationId);
    Location updateLocation(Long id, Location location);
    void deleteLocation(Long locationId);
    void deleteAllLocations();
    List<Location> searchLocation(String query);
}
