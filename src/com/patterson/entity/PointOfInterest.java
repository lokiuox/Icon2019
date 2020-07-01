package com.patterson.entity;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

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
        double random = ThreadLocalRandom.current().nextDouble();
        if (random <= 0.1) {
            img = new ImageIcon("resources/marker/poste.png").getImage();
        } else if (random <= 0.3) {
            img = new ImageIcon("resources/marker/scuola.png").getImage();
        } else if (random <= 0.5) {
            img = new ImageIcon("resources/marker/lavoro.png").getImage();
        } else if (random <= 0.7) {
            img = new ImageIcon("resources/marker/ufficio.png").getImage();
        } else {
            img = new ImageIcon("resources/marker/negozio.png").getImage();
        }
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
