package com.example.gps.model;

import org.springframework.data.geo.Circle;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class QuadTree {
    public static final int mapSize = 1000;
    public static final int nodeCapacity = 1;

    Node root;

    private class Node {
        boolean divided;
        Rectangle2D.Double rect;
        ArrayList<Location> locations;
        Node nwNode, neNode, swNode, seNode;


        Node(double x, double y, double size) {
            this.divided = false;
            this.rect = new Rectangle2D.Double(x, y, size, size);
            this.locations = new ArrayList<Location>();
        }

        //Node x,y are at the center
        Node(double x, double y, double size, Location location) {
            this.divided = false;
            this.rect = new Rectangle2D.Double(x, y, size, size);
            this.locations = new ArrayList<Location>();
            this.locations.add(location);
        }
    }

    public QuadTree() {
        root = new Node(0, 0, mapSize);
    }

    public Location checkCoordsUsed(double x, double y) {
        ArrayList<Location> locsArr = getNode(root, x ,y).locations;

        for (Location currLoc : locsArr) {
            if (currLoc.getLatitude() == x && currLoc.getLongitude() == y) {
                return currLoc;
            }
        }

        return null;
    }

    public boolean locationExists(Location loc) {
        ArrayList<Location> locsArr = getNode(root,
                loc.getLatitude(), loc.getLongitude()).locations;

        for (Location currLoc : locsArr) {
            if (currLoc.equals(loc)) {
                return true;
            }
        }

        return false;
    }

    public void insert(Location location) {
        //root = insert(root, 0, 0, mapSize, location);
        insert(root, 0, 0, mapSize, location);
    }

    private void insert(Node currNode, double rectX, double rectY, double size, Location location) {
        if (!currNode.divided) {
            currNode.locations.add(location);
            if (currNode.locations.size() > nodeCapacity) {
                divide(currNode, size);
            }
            //return new Node(rectX, rectY, size, location);
        } else {
            try {
                String pickedNode = pickNodeHelper(location.getLatitude(), location.getLongitude(), currNode.rect);

                switch(pickedNode) {
                    case "NW":
                        insert(currNode.nwNode, rectX, rectY, size/2, location);
                        break;
                    case "NE":
                        insert(currNode.neNode, rectX + size/2,
                                rectY, size/2, location);
                        break;
                    case "SW":
                        insert(currNode.swNode, rectX,
                                rectY + size/2, size/2, location);
                        break;
                    case "SE":
                        insert(currNode.seNode, rectX + size/2,
                                rectY + (int) size/2, size/2, location);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void divide(Node currNode, double size) {

        Rectangle2D.Double currRect = currNode.rect;
        double halfSize = size / 2;
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

    public void remove(Location loc) {
        remove(root, loc);
    }

    private void remove(Node currNode, Location loc) {
        if (!currNode.divided) {
            currNode.locations.remove(loc);
        } else {
            try {
                String pickedNode = pickNodeHelper(loc.getLatitude(),
                        loc.getLongitude(), currNode.rect);
                Node nextNode = null;

                switch (pickedNode) {
                    case "NW":
                        nextNode = currNode.nwNode;
                        //remove(currNode.nwNode, loc);
                        break;
                    case "NE":
                        nextNode = currNode.neNode;
                        //remove(currNode.neNode, loc);
                        break;
                    case "SW":
                        nextNode = currNode.swNode;
                        //remove(currNode.swNode, loc);
                        break;
                    case "SE":
                        nextNode = currNode.seNode;
                        //remove(currNode.seNode, loc);
                        break;
                }

                remove(nextNode, loc);
                if (!stillDivided(currNode)) {
                    currNode.divided = false;
                    currNode.locations.addAll(currNode.nwNode.locations);
                    currNode.locations.addAll(currNode.neNode.locations);
                    currNode.locations.addAll(currNode.swNode.locations);
                    currNode.locations.addAll(currNode.seNode.locations);
                    currNode.nwNode = null;
                    currNode.neNode = null;
                    currNode.swNode = null;
                    currNode.seNode = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean stillDivided(Node currNode) {
        if (currNode.nwNode.divided || currNode.neNode.divided ||
            currNode.swNode.divided || currNode.seNode.divided) {
            return true;
        } else if (currNode.nwNode.locations.size() +
                    currNode.neNode.locations.size() +
                    currNode.swNode.locations.size() +
                    currNode.seNode.locations.size() > nodeCapacity) {
            return true;
        } else {
            return false;
        }
    }

    private Node getNode(Node currNode, double x, double y) {
        if (!currNode.divided) {
            return currNode;
        } else {
            try {
                String pickedNode = pickNodeHelper(x, y, currNode.rect);

                switch (pickedNode) {
                    case "NW":
                        return getNode(currNode.nwNode, x, y);
                    case "NE":
                        return getNode(currNode.neNode, x, y);
                    case "SW":
                        return getNode(currNode.swNode, x, y);
                    case "SE":
                        return getNode(currNode.seNode, x, y);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public ArrayList<Location> getNodesWithinCircle(Circle circle) {
        return getNodesWithinCircle(root, circle);
    }

    private ArrayList<Location> getNodesWithinCircle(Node currNode, Circle circle) {
        ArrayList<Location> locsInCircle = new ArrayList<Location>();

        if (!currNode.divided) {
            for (Location currLoc : currNode.locations) {
                Point2D.Double locPoint = new Point2D.Double(currLoc.getLatitude(), currLoc.getLongitude());
                Point2D.Double circlePoint = new Point2D.Double(circle.getCenter().getX(), circle.getCenter().getY());
                if (locPoint.distance(circlePoint) <= circle.getRadius().getValue()) {
                    locsInCircle.add(currLoc);
                }
            }

            return locsInCircle;
        } else {
            if (rectIntersectsCircle(currNode.nwNode.rect, circle)) {
                locsInCircle.addAll(getNodesWithinCircle(currNode.nwNode, circle));
            }
            if (rectIntersectsCircle(currNode.neNode.rect, circle)) {
                locsInCircle.addAll(getNodesWithinCircle(currNode.neNode, circle));
            }
            if (rectIntersectsCircle(currNode.swNode.rect, circle)) {
                locsInCircle.addAll(getNodesWithinCircle(currNode.swNode, circle));
            }
            if (rectIntersectsCircle(currNode.seNode.rect, circle)) {
                locsInCircle.addAll(getNodesWithinCircle(currNode.seNode, circle));
            }
        }

        return locsInCircle;
    }

    private String pickNodeHelper(double locX, double locY, Rectangle2D.Double rect) throws Exception {
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

    private boolean rectIntersectsCircle(Rectangle2D.Double rect, Circle circle) {
        double x1 = rect.x;
        double y1 = rect.y;
        double x2 = rect.x + rect.width;
        double y2 = rect.y + rect.height;

        double xN = Math.max(x1, Math.min(circle.getCenter().getX(), x2));
        double yN = Math.max(y1, Math.min(circle.getCenter().getY(), y2));

        double xD = xN - circle.getCenter().getX();
        double yD = yN - circle.getCenter().getY();
        double radius = circle.getRadius().getValue();

        return ((xD * xD + yD * yD) <= (radius * radius));
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



