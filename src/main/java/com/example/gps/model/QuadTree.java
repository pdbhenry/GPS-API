package com.example.gps.model;

import java.awt.*;
import java.util.ArrayList;

public class QuadTree {
    public static final int mapSize = 1000;
    public static final int nodeCapacity = 1;

    Node root;

    private class Node {
        boolean divided;
        Rectangle rect;
        ArrayList<Location> locations;
        Node nwNode, neNode, swNode, seNode;


        Node(int x, int y, int size) {
            this.divided = false;
            this.rect = new Rectangle(x, y, size, size);
            this.locations = new ArrayList<Location>();
        }

        //Node x,y are at the center
        Node(int x, int y, int size, Location location) {
            this.divided = false;
            this.rect = new Rectangle(x, y, size, size);
            this.locations = new ArrayList<Location>();
            this.locations.add(location);
        }
    }

    public QuadTree() {
        root = new Node(0, 0, mapSize);
    }

    public void insert(Location location) {
        //root = insert(root, 0, 0, mapSize, location);
        insert(root, 0, 0, mapSize, location);
    }

    public void insert(Node currNode, Location location) {
        insert(currNode, currNode.rect.x, currNode.rect.y, currNode.rect.width, location);
    }

    private void insert(Node currNode, int rectX, int rectY, int size, Location location) {
        if (!currNode.divided) {
            currNode.locations.add(location);
            if (currNode.locations.size() > nodeCapacity) {
                divide(currNode, size);
            }
            //return new Node(rectX, rectY, size, location);
        } else {
            double locX = location.getLatitude();
            double locY = location.getLongitude();

            try {
                String pickedNode = pickNodeHelper(location.getLatitude(), location.getLongitude(), currNode.rect);

                switch(pickedNode) {
                    case "NW":
                        insert(currNode.nwNode, rectX, rectY, size/2, location);
                    case "NE":
                        insert(currNode.neNode, rectX + size/2,
                                rectY, size/2, location);
                    case "SW":
                        insert(currNode.swNode, rectX,
                                rectY + size/2, size/2, location);
                    case "SE":
                        insert(currNode.seNode, rectX + size/2,
                                rectY + (int) size/2, size/2, location);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void divide(Node currNode, int size) {

        Rectangle currRect = currNode.rect;
        int halfSize = size / 2;
        currNode.nwNode = new Node(currRect.x, currRect.y,  halfSize);
        currNode.neNode = new Node(currRect.x + halfSize, currRect.y,  halfSize);
        currNode.swNode = new Node(currRect.x, currRect.y + halfSize,  halfSize);
        currNode.seNode = new Node(currRect.x + halfSize, currRect.y + halfSize,  halfSize);

        currNode.divided = true;

        for (Location location : currNode.locations) {
            insert(location);
        }

        currNode.locations.removeAll(currNode.locations);
    }

    public Location checkCoordsUsed(double x, double y) {
        ArrayList<Location> locsArr = getNodeLocations(root, x ,y);

        for (Location currLoc : locsArr) {
            if (currLoc.getLatitude() == x && currLoc.getLongitude() == y) {
                return currLoc;
            }
        }

        return null;
    }

    public boolean locationExists(Location location) {
        ArrayList<Location> locsArr = getNodeLocations(root,
                location.getLatitude() ,location.getLongitude());

        for (Location currLoc : locsArr) {
            if (currLoc.equals(location)) {
                return true;
            }
        }

        return false;
    }

    private ArrayList<Location> getNodeLocations(Node currNode, double x, double y) {
        if (!currNode.divided) {
            /*for (Location currLoc : currNode.locations) {
                if (currLoc.equals(location)) {
                    return currLoc;
                }
            }*/
            return currNode.locations;
        } else {

            try {
                String pickedNode = pickNodeHelper(x, y, currNode.rect);

                switch (pickedNode) {
                    case "NW":
                        return getNodeLocations(currNode.nwNode, x, y);
                    case "NE":
                        return getNodeLocations(currNode.neNode, x, y);
                    case "SW":
                        return getNodeLocations(currNode.swNode, x, y);
                    case "SE":
                        return getNodeLocations(currNode.seNode, x, y);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private String pickNodeHelper(double locX, double locY, Rectangle rect) throws Exception {
        if ((locX <= (rect.getX() + (double) rect.width / 2)) && (locY <= (rect.getY() + (double) rect.height / 2))) {
            return "NW";
        } else if ((locX > (rect.getX() + (double) rect.width / 2)) && (locY <= (rect.getY() + (double) rect.height / 2))) {
            return "NE";
        } else if ((locX <= (rect.getX() + (double) rect.width / 2)) && (locY > (rect.getY() + (double) rect.height / 2))) {
            return "SW";
        } else if ((locX > (rect.getX() + (double) rect.width / 2)) && (locY > (rect.getY() + (double) rect.height / 2))) {
            return "SE";
        }

        throw new Exception("No place for location");
    }
}

/*
public boolean findInQuad(Location location) {
    return (findInQuad(root, location) != null);
}

private Node findInQuad(Node currNode, Location location) {
    if (currNode.location.equals(location)) {
        return currNode;
    }

    double locX = location.getLatitude();
    double locY = location.getLongitude();
    if (locX > )
        return currNode;
}
*/



