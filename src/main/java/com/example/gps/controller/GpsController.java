package com.example.gps.controller;

import com.example.gps.model.Location;
import com.example.gps.service.GpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public ResponseEntity<List<Location>> getLocations(Model model) {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/searchSlow/{id}")
    public ResponseEntity<Location> searchLocation(@PathVariable Long id) {

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addLocation")
    public ResponseEntity<Location> addLocation(@RequestBody Location location) {
        return new ResponseEntity<>(HttpStatus.CREATED); //locationObj, HttpStatus.CREATED);
    }

    @PutMapping("/updateLocation/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody Location location) {

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteLocation/{id}")
    public ResponseEntity<HttpStatus> deleteLocation(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteAllLocations")
    public ResponseEntity<HttpStatus> deleteAllLocations()
    {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
