package com.example.gps.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setRandomCoords(int mapSize) {
        Random r = new Random();

        //Generate random coords within map boundaries, rounded to 2 decimal places
        double randLat = Math.round(r.nextDouble(mapSize) * 100) / 100.0;
        double randLong = Math.round(r.nextDouble(mapSize) * 100) / 100.0;
        this.setLatitude(randLat);
        this.setLongitude(randLong);
    }
}
