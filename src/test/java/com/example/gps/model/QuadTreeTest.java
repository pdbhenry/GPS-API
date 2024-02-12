package com.example.gps.model;

import org.junit.jupiter.api.Test;
import org.springframework.data.geo.Circle;

import java.util.ArrayList;

public class QuadTreeTest {

    QuadTree quadTree = new QuadTree();

    @Test
    public void insertIntoQuadTree() {
        Location loc1 = new Location("McDonald's", 20.0, 12.0);
        Location loc2 = new Location("McDonald's", 20.0, 12.0);
        quadTree.insert(loc1);

        assert(quadTree.locationExists(loc1));
        assert(quadTree.locationExists(loc2));
        assert(quadTree.checkCoordsUsed(loc1.getLatitude(), loc1.getLongitude()).equals(loc1));
    }

    @Test
    public void insertMultipleIntoQuadTree() {
        Location loc1 = new Location("McDonald's", 230.0, 10.0);
        Location loc2 = new Location("Burger King", 100.0, 140.0);
        Location loc3 = new Location("Long John Silver's", 270.0, 270.0);
        Location loc4 = new Location("Chipotle", 550.0, 550.0);

        quadTree.insert(loc1);
        quadTree.insert(loc2);
        quadTree.insert(loc3);
        quadTree.insert(loc4);

        assert(quadTree.locationExists(loc1));
        assert(quadTree.locationExists(loc2));
        assert(quadTree.locationExists(loc3));
        assert(quadTree.locationExists(loc4));
    }

    @Test void insertDeepIntoQuadTree() {
        Location loc1 = new Location("McDonald's", 230.0, 10.0);
        Location loc2 = new Location("Burger King", 230.05, 10.0);
        quadTree.insert(loc1);
        quadTree.insert(loc2);
        assert(quadTree.locationExists(loc1));
        assert(quadTree.locationExists(loc2));
    }

    @Test
    public void removeFromQuadTree() {
        Location loc1 = new Location("McDonald's", 230.0, 10.0);

        quadTree.insert(loc1);
        assert(quadTree.locationExists(loc1));

        quadTree.remove(loc1);
        assert(!quadTree.locationExists(loc1));
    }

    @Test
    public void getLocsWithinCircle() {
        Location loc1 = new Location("McDonald's", 210.0, 25.0);
        Location loc2 = new Location("Burger King", 230.05, 10.0);
        Location loc3 = new Location("Long John Silver's", 400.0, 150.0);
        Location loc4 = new Location("Chipotle", 550.0, 550.0);
        Circle circle = new Circle(250, 150, 150);

        quadTree.insert(loc1);
        quadTree.insert(loc2);
        quadTree.insert(loc3);
        quadTree.insert(loc4);

        ArrayList<Location> locsInCircle = quadTree.getNodesWithinCircle(circle);
        assert(locsInCircle.size() == 3);
    }

    @Test
    public void removeDeepNodeFromQuadTree() {
        Location loc1 = new Location("McDonald's", 230.0, 10.0);
        Location loc2 = new Location("Burger King", 230.1, 10.0);
        Location loc3 = new Location("Chipotle", 550.0, 550.0);

        quadTree.insert(loc1);
        quadTree.insert(loc2);
        quadTree.insert(loc3);

        assert(quadTree.locationExists(loc1));
        assert(quadTree.locationExists(loc2));
        assert(quadTree.locationExists(loc3));

        quadTree.remove(loc2);
        assert(quadTree.locationExists(loc1));
        assert(!quadTree.locationExists(loc2));
        assert(quadTree.locationExists(loc3));
    }

    @Test
    public void getNearestSimple() {
        Location loc1 = new Location("McDonald's", 230.0, 10.0);
        Location loc2 = new Location("Burger King", 100.0, 140.0);
        Location loc3 = new Location("Long John Silver's", 270.0, 270.0);
        Location loc4 = new Location("Chipotle", 550.0, 550.0);

        quadTree.insert(loc1);
        quadTree.insert(loc2);
        quadTree.insert(loc3);
        quadTree.insert(loc4);

        Location nearestLoc = quadTree.getNearestLocationToLocation(loc4);
        assert(nearestLoc.equals(loc3));
    }

    @Test
    public void getNearestDeep() {
        Location loc1 = new Location("McDonald's", 230.0, 10.0);
        Location loc2 = new Location("Burger King", 230.1, 10.0);
        Location loc3 = new Location("Chipotle", 550.0, 550.0);

        quadTree.insert(loc1);
        quadTree.insert(loc2);
        quadTree.insert(loc3);

        Location nearestLoc = quadTree.getNearestLocationToLocation(loc1);
        assert(nearestLoc.equals(loc2));
    }

    @Test
    public void getNearestAcrossQuadrants() {
        Location loc1 = new Location("McDonald's", 230.0, 10.0);
        Location loc2 = new Location("Burger King", 200.0, 15.0);
        Location loc3 = new Location("Chipotle", 255.0, 10.0);

        quadTree.insert(loc1);
        quadTree.insert(loc2);
        quadTree.insert(loc3);

        Location nearestLoc = quadTree.getNearestLocationToLocation(loc1);
        assert(nearestLoc.equals(loc3));
    }

    @Test
    public void getNearestNoResult() {
        Location loc1 = new Location("McDonald's", 230.0, 10.0);

        quadTree.insert(loc1);

        Location nearestLoc = quadTree.getNearestLocationToLocation(loc1);
        assert(nearestLoc == null);
    }

}
