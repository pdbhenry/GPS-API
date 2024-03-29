package com.example.gps.service.impl;

import com.example.gps.model.Location;
import com.example.gps.model.Names;
import com.example.gps.model.QuadTree;
import com.example.gps.repository.GpsRepo;
import com.example.gps.service.GpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GpsServiceImpl implements GpsService {

    public static final int mapSize = 1000;
    private GpsRepo gpsRepo;
    private QuadTree quadTree = new QuadTree(mapSize, 1);
    private Names names = new Names();

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
        Location loc = gpsRepo.save(location);
        saveToQuadtree(location);
        return loc;
    }

    @Override
    public void saveToQuadtree(Location location) {
        quadTree.insert(location);
    }

    @Override
    public void populateTable(int numLocs) {
        for (int i = 0; i < numLocs; i++) {
            Location loc = new Location();
            loc.setName(names.getRandomName());
            loc.setRandomCoords(mapSize);
            saveLocation(loc);
        }
    }

    @Override
    public Location findLocationById(Long locationId) {
        Optional<Location> locationObj = gpsRepo.findById(locationId);
        return locationObj.orElse(null);
    }

    @Override
    public boolean findLocationByCoordsSlow(Location location) {
        for (Location currLocation : gpsRepo.findAll()) {
            if (currLocation.getLatitude() == location.getLatitude() &&
                    currLocation.getLongitude() == location.getLongitude()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean findLocationByCoords(Location location) {
        return quadTree.locationExists(location);
    }

    @Override
    public ArrayList<Location> findLocsInCircleSlow(Circle circle) {
        ArrayList<Location> locsInCircle = new ArrayList<Location>();

        for (Location currLocation : gpsRepo.findAll()) {
            Point2D.Double locPoint = new Point2D.Double(currLocation.getLatitude(), currLocation.getLongitude());
            Point2D.Double circlePoint = new Point2D.Double(circle.getCenter().getX(), circle.getCenter().getY());

            if (locPoint.distance(circlePoint) <= circle.getRadius().getValue()) {
                locsInCircle.add(currLocation);
            }
        }

        return locsInCircle;
    }

    @Override
    public ArrayList<Location> findLocsInCircle(Circle circle) {
        return quadTree.getNodesWithinCircle(circle);
    }

    @Override
    public Location updateLocation(Long id, Location location) {
        Optional<Location> locationOptional = gpsRepo.findById(id);
        if (locationOptional.isPresent()) {
            Location location1 = locationOptional.get();
            quadTree.remove(location1);

            location1.setName(location.getName());
            location1.setLatitude(location.getLatitude());
            location1.setLongitude(location.getLongitude());
            quadTree.insert(location);

            return gpsRepo.save(location1);
        }

        return null;
    }

    @Override
    public void deleteLocation(Long locationId) {
        Optional<Location> locationObj = gpsRepo.findById(locationId);
        if (locationObj.isPresent()) {
            Location loc = locationObj.get();
            gpsRepo.delete(loc);
            quadTree.remove(loc);
        }
    }

    @Override
    public void deleteAllLocations() {
        gpsRepo.deleteAll();
        quadTree = new QuadTree(mapSize, 1);
    }

    @Override
    public Location getClosestLocationSlow(Long locationId) {
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
    public Location getClosestLocation(Long locationId) {
        Location closestLoc = null;
        Optional<Location> locationObj = gpsRepo.findById(locationId);

        if (locationObj.isPresent()) {
            Location loc = locationObj.get();
            closestLoc = quadTree.getNearestLocationToLocation(loc);
        }

        return closestLoc;
    }
}
