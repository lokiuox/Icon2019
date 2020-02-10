package com.patterson.world;

import com.patterson.entity.Car;
import com.patterson.entity.IScenario;
import com.patterson.entity.Intersection;
import com.patterson.entity.Road;
import com.patterson.utility.KnowledgeBase;

import org.json.*;
import java.awt.Graphics2D;
import java.io.*;
import java.util.*;

public class Scenario implements IScenario {
    private String name;
    private Map<String, Intersection> intersections = new HashMap<>();
    private Map<String, Road> roads = new HashMap<>();
    private List<Car> cars = new LinkedList<>();
    private String kb_path = "resources/KB.pl";

    Scenario() {
        init();
    }

    Scenario(Collection<Road> r, Collection<Intersection> i, Collection<Car> c) {
        if (r != null)
            this.addRoads(r);
        if (i != null)
            this.addIntersections(i);
        if (c != null)
            this.addCars(c);
        init();
    }

    Scenario(Collection<Road> r, Collection<Intersection> i, Collection<Car> c, String kb_path) {
        if (r != null)
            this.addRoads(r);
        if (i != null)
            this.addIntersections(i);
        if (c != null)
            this.addCars(c);
        this.kb_path = kb_path;
        init();
    }

    private void init() {
        KnowledgeBase.init(kb_path);
    }

    public void setName(String s) {
        this.name = s;
    }

    public String getName() {
        return this.name;
    }

    public void setKB(String s) {
        this.kb_path = s;
    }

    public Road getRoadByID(String id) {
        return roads.get(id);
    }

    public List<Road> getRoads() {
        return new ArrayList<Road>(roads.values());
    }

    public Scenario addRoad(Road r) {
        roads.put(r.getID(), r);
        return this;
    }

    public Scenario addRoads(Collection<Road> list) {
        for (Road r : list)
            roads.put(r.getID(), r);
        return this;
    }

    public Intersection getIntersectionByID(String id) {
        return intersections.get(id);
    }

    public List<Intersection> getIntersections() {
        return new ArrayList<Intersection>(intersections.values());
    }

    public Scenario addIntersection(Intersection i) {
        intersections.put(i.getID(), i);
        return this;
    }

    public Scenario addIntersections(Collection<Intersection> list) {
        for (Intersection i : list)
            intersections.put(i.getID(), i);
        return this;
    }

    public Car getCarByID(String id) {
        for (Car c: cars)
            if (c.getID().equals(id))
                return c;
        return null;
    }

    public List<Car> getCars() {
        return cars;
    }

    public Scenario addCar(Car c) {
        cars.add(c);
        return this;
    }

    public Scenario addCars(Collection<Car> list) {
        cars.addAll(list);
        return this;
    }

    public void toJSON(Writer writer) {
        JSONObject scenario = new JSONObject();
        scenario.put("name", name);
        scenario.put("kb", kb_path);

        JSONArray ja_roads = new JSONArray();
        for (Road r: roads.values())
            ja_roads.put(r.toJSONObject());

        JSONArray ja_intersections = new JSONArray();
        for (Intersection i: intersections.values())
            ja_intersections.put(i.toJSONObject());

        JSONArray ja_cars = new JSONArray();
        for (Car c: cars)
            ja_cars.put(c.toJSONObject());

        scenario.put("roads", ja_roads);
        scenario.put("intersections", ja_intersections);
        scenario.put("cars", ja_cars);
        scenario.write(writer);
    }

    public String toJSON() {
        StringWriter sw = new StringWriter();
        toJSON(sw);
        return sw.toString();
    }

    public void export(String path) {
        System.err.print("Esporto...");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            this.toJSON(writer);
            System.err.println("OK.");
            writer.close();
        } catch (IOException e) {
            System.err.println("Errore nella scrittura del file.");
            e.printStackTrace();
        }
    }


    public void draw(Graphics2D g) {
        for (Intersection i : intersections.values()) i.draw(g);
        for (Road r : roads.values()) r.draw(g);
        for (Car c : cars) c.draw(g);
    }

    public void tick() {
        for (Car c : cars) c.tick();
    }
}
