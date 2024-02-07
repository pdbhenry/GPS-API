package com.example.gps.model;

import org.junit.jupiter.api.Test;

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

}
