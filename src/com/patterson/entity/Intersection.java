package com.patterson.entity;

import com.patterson.utility.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

public class Intersection implements Entity {

    String ID;
    Point2D position = new Point2D.Float(0, 0);
    int width = 0;
    int height = 0;
    Set<Road> roads = new HashSet<>();

    protected Image img;

    public Intersection(String id, int x, int y, int w, int h) {
        ID = id;
        position.setLocation(x,y);
        width = w;
        height = h;
        loadImage();
    }

    public String getID() {
        return ID;
    }

    public Set<Road> getRoads() {
        return roads;
    }

    @Override
    public void draw(Graphics2D g) {
        for (int i=0; i<=width; i+=16) {
            for (int j=0; j<=width; j+=16) {
                g.drawImage(img, (int)position.getX()+i, (int)position.getY()+j, null);
            }
        }
    }

    protected void loadImage() {
        img = new ImageIcon("resources/road/asphalt.png").getImage();
    }
}
