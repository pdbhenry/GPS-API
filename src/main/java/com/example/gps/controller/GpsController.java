package com.example.gps.controller;

import com.example.gps.model.Location;
import com.example.gps.service.GpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GpsController {

    private GpsService gpsService;

    @Autowired
    public GpsController(GpsService gpsService) {
        this.gpsService = gpsService;
    }

    @GetMapping
    public ResponseEntity<List<Location>> getLocations() {
        List<Location> result = gpsService.findAllLocations();
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/searchSlow/{id}")
    public ResponseEntity<Location> searchLocation(@PathVariable Long id) {
        Location locationObj = gpsService.findLocationById(id);
        if (locationObj != null) {
            return new ResponseEntity<>(locationObj, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getClosestLocationSlow/{id}")
    public ResponseEntity<Location> getClosestLocationSlow(@PathVariable Long id) {
        Location closestLocation = gpsService.closestLocationSlow(id);

        if (closestLocation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(closestLocation, HttpStatus.FOUND);
    }

    @PostMapping("/addLocation")
    public ResponseEntity<Location> addLocation(@RequestBody Location location) {
        Location locationObj = gpsService.saveLocation(location);
        return new ResponseEntity<>(locationObj, HttpStatus.CREATED);
    }

    @PutMapping("/updateLocation/{id}")
    public ResponseEntity<HttpStatus> updateLocation(@PathVariable Long id, @RequestBody Location location) {
        Location updatedLocation = gpsService.updateLocation(id, location);
        if (updatedLocation != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteLocation/{id}")
    public ResponseEntity<HttpStatus> deleteLocation(@PathVariable Long id) {
        gpsService.deleteLocation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteAllLocations")
    public ResponseEntity<HttpStatus> deleteAllLocations()
    {
        gpsService.deleteAllLocations();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
