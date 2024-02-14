package com.example.gps.controller;

import com.example.gps.model.Location;
import com.example.gps.service.GpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/search/{id}")
    public ResponseEntity<Location> searchLocById(@PathVariable Long id) {
        Location locationObj = gpsService.findLocationById(id);
        if (locationObj != null) {
            return new ResponseEntity<>(locationObj, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/searchSlow")
    public ResponseEntity<Location> searchLocByLocSlow(@RequestBody Location location) {
        if (gpsService.findLocationByCoordsSlow(location)) {
            return new ResponseEntity<>(HttpStatus.IM_USED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search")
    public ResponseEntity<Location> searchLocByLoc(@RequestBody Location location) {
        if (gpsService.findLocationByCoords(location)) {
            return new ResponseEntity<>(HttpStatus.IM_USED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getClosestLocSlow/{id}")
    public ResponseEntity<Location> getClosestLocSlow(@PathVariable Long id) {
        Location closestLocation = gpsService.getClosestLocationSlow(id);

        if (closestLocation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(closestLocation, HttpStatus.FOUND);
    }

    @GetMapping("/getClosestLoc/{id}")
    public ResponseEntity<Location> getClosestLoc(@PathVariable Long id) {
        Location closestLocation = gpsService.getClosestLocation(id);

        if (closestLocation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(closestLocation, HttpStatus.FOUND);
    }

    @GetMapping("/getLocsWithinCircleSlow")
    public ResponseEntity<ArrayList<Location>> getLocsWithinCircleSlow(@RequestBody Map<String, Double> circleMap) {
        Circle circle = new Circle(circleMap.get("cX"), circleMap.get("cY"), circleMap.get("r"));
        ArrayList<Location> locsInCircle = gpsService.findLocsInCircleSlow(circle);

        return new ResponseEntity<>(locsInCircle, HttpStatus.OK);
    }

    @GetMapping("/getLocsWithinCircle")
    public ResponseEntity<ArrayList<Location>> getLocsWithinCircle(@RequestBody Map<String, Double> circleMap) {
        Circle circle = new Circle(circleMap.get("cX"), circleMap.get("cY"), circleMap.get("r"));
        ArrayList<Location> locsInCircle = gpsService.findLocsInCircle(circle);

        return new ResponseEntity<>(locsInCircle, HttpStatus.OK);
    }

    @PostMapping("/addLoc")
    public ResponseEntity<Location> addLocation(@RequestBody Location location) {
        if (gpsService.findLocationByCoords(location)) {
            return new ResponseEntity<>(HttpStatus.IM_USED);
        }

        Location locationObj = gpsService.saveLocation(location);
        return new ResponseEntity<>(locationObj, HttpStatus.CREATED);
    }

    @PostMapping("/populateLocs/{numLocs}")
    public ResponseEntity<HttpStatus> populateLocs(@PathVariable int numLocs) {
        gpsService.populateTable(numLocs);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/populateQuadtree")
    public ResponseEntity<HttpStatus> populateQuadtree() {
        for (Location loc : gpsService.findAllLocations()) {
            gpsService.saveToQuadtree(loc);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updateLoc/{id}")
    public ResponseEntity<HttpStatus> updateLocation(@PathVariable Long id, @RequestBody Location location) {
        Location updatedLocation = gpsService.updateLocation(id, location);
        if (updatedLocation != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteLoc/{id}")
    public ResponseEntity<HttpStatus> deleteLocation(@PathVariable Long id) {
        gpsService.deleteLocation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteAllLocs")
    public ResponseEntity<HttpStatus> deleteAllLocations()
    {
        gpsService.deleteAllLocations();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
