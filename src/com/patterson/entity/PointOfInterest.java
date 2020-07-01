package com.patterson.entity;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class PointOfInterest implements IEntity {
    private static int counter = 0;
    protected String ID;
    protected Point position = new Point(0, 0);
    protected Image img;
    protected Set<Road> roads = new HashSet<>();

    public String getID() {
        return ID;
    }
    public void draw(Graphics2D g) {
        g.drawImage(img, position.x-16, position.y-16, null);
    }

    private static void updateCounter(String id) {
        counter = Math.max(counter, Integer.parseInt(id.substring(1)));
    }

    protected void loadImage() {
        img = new ImageIcon("resources/marker/marker.png").getImage();
    }

    public PointOfInterest(String id, int x, int y) {
        updateCounter(id);
        ID = id;
        position.setLocation(x,y);
        loadImage();
    }

    public Point getPosition() { return position; }

    public void setPosition(Point p) { position = new Point(p); }

    public static String nextID() { return "p" + ++counter; }

    public Set<Road> getRoads() { return roads; }

    public void addRoad(Road r) { roads.add(r); }
}
