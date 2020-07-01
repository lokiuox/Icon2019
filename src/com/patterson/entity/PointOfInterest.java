package com.patterson.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PointOfInterest implements IEntity {
    private static int counter = 0;
    protected String ID;
    protected Point position = new Point(0, 0);
    protected Image img;
    protected Road road = null;

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

    public PointOfInterest(JSONObject jo_poi) {
        this(jo_poi.getString("id"),
                jo_poi.getInt("posX"),
                jo_poi.getInt("posY"));
    }

    public Point getPosition() { return position; }

    public void setPosition(Point p) { position = new Point(p); }

    public static String nextID() { return "p" + ++counter; }

    public Road getRoad() { return road; }

    public void setRoad(Road r) { road = r; }

    public JSONObject toJSONObject() {
        JSONObject poi = new JSONObject();
        poi.put("id", ID);
        poi.put("posX", position.getX());
        poi.put("posY", position.getY());
        poi.put("road", road.getID());
        return poi;
    }

    public String toJSON() {
        JSONObject json_object = this.toJSONObject();
        return json_object.toString();
    }
}
