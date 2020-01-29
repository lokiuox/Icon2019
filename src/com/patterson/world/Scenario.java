package com.patterson.world;

import com.patterson.entity.Road;

import java.awt.*;
import java.util.*;

public class Scenario {
    private Dictionary<String,Road> roads = new Hashtable<>();

    Scenario() {
        initRoads();
    }

    private void initRoads() {
        roads.put("R0", new Road(32,5*16,0, 20*16));
        roads.put("R1", new Road(32+20*16+16,5*16+16,3, 20*16));
        roads.put("R2", new Road(32+20*16, 5*16+32+20*16, 2, 10*16));
    }

    public void draw(Graphics2D g) {

        Enumeration<Road> e = roads.elements();

        while (e.hasMoreElements()) {
            e.nextElement().draw(g);
        }
    }

    public Road getRoadByID(String s) {
        return roads.get(s);
    }
}
