package com.example.gps.service.impl;

import com.example.gps.model.Location;
import com.example.gps.repository.GpsRepo;
import com.example.gps.service.GpsService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GpsServiceImpl implements GpsService {

    private GpsRepo gpsRepo;

    @Autowired
    public GpsServiceImpl(GpsRepo gpsRepo) {
        this.gpsRepo = gpsRepo;
    }

    @Override
    public List<Location> findAllLocations() {
        return gpsRepo.findAll();
    }

    @Override
    public Location saveLocation(Location location) {
        return gpsRepo.save(location);
    }

    @Override
    public Location findLocationById(Long locationId) {
        Optional<Location> locationObj = gpsRepo.findById(locationId);
        return locationObj.orElse(null);
    }

    @Override
    public Location updateLocation(Long id, Location location) {
        Optional<Location> locationOptional = gpsRepo.findById(id);
        if (locationOptional.isPresent()) {
            Location location1 = locationOptional.get();
            location1.setName(location.getName());
            location1.setLatitude(location.getLatitude());
            location1.setLongitude(location.getLongitude());
            return gpsRepo.save(location1);
        }

        return null;
    }

    @Override
    public void deleteLocation(Long locationId) {
        Optional<Location> locationObj = gpsRepo.findById(locationId);
        locationObj.ifPresent(location -> gpsRepo.delete(location));
    }

    @Override
    public void deleteAllLocations() {
        gpsRepo.deleteAll();
    }

    @Override
    public Location closestLocationSlow(Long locationId) {
        Location closestLocation = null;
        Optional<Location> locationObj = gpsRepo.findById(locationId);

        if (locationObj.isPresent()) {
            Location location = locationObj.get();
            double shortestDist = Integer.MAX_VALUE;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            List<Location> allLocations = gpsRepo.findAll();

            for (Location currLocation : allLocations) {
                if (currLocation.getId().longValue() != locationId) {
                    double currDistance = Math.hypot(latitude-currLocation.getLatitude(),
                            longitude-currLocation.getLongitude());

                    if (currDistance < shortestDist) {
                        shortestDist = currDistance;
                        closestLocation = currLocation;
                    }
                }
            }
        }

        return closestLocation;
    }

    @Override
    public Location closestLocation(Long locationId) {
        return null;
    }
}
