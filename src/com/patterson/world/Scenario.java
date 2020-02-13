package com.patterson.world;

import com.patterson.entity.*;
import com.patterson.utility.KnowledgeBase;

import org.json.*;
import java.awt.Graphics2D;
import java.io.*;
import java.util.*;

public class Scenario implements IScenario {
    private String name = "New Scenario";
    private Map<String, Intersection> intersections = new HashMap<>();
    private Map<String, Road> roads = new HashMap<>();
    private List<Car> cars = new LinkedList<>();
    private String kb_path = "resources/KB.pl";

    public Scenario() {
        init();
    }

    public Scenario(String json_file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(json_file));

            //Getting base contents
            JSONTokener jsonTokener = new JSONTokener(reader);
            JSONObject jo_scenario = new JSONObject(jsonTokener);
            JSONArray ja_roads = jo_scenario.getJSONArray("roads");
            JSONArray ja_intersections = jo_scenario.getJSONArray("intersections");
            JSONArray ja_cars = jo_scenario.getJSONArray("cars");
            String name = jo_scenario.getString("name");
            String kb_path = jo_scenario.getString("kb");
            this.kb_path = kb_path;

            //Creating containing structures
            Map<String, Road> roads = new HashMap<>();
            Map<String, Intersection> intersections = new HashMap<>();
            Map<String, Car> cars = new HashMap<>();


            //Creating entities
            for (int i = 0; i < ja_intersections.length(); i++) {
                JSONObject obj = ja_intersections.getJSONObject(i);
                Intersection intersection = new Intersection(obj);
                intersections.put(obj.getString("id"), intersection);
                this.addIntersection(intersection);
            }

            for (int i = 0; i < ja_roads.length(); i++) {
                JSONObject obj = ja_roads.getJSONObject(i);
                Road r = new Road(obj);
                String intersection_id = obj.getString("intersection");
                if (intersection_id!=null && !intersection_id.equals("null")) {
                    Intersection intersection = intersections.get(intersection_id);
                    r.setIntersection(intersection);
                }
                roads.put(obj.getString("id"), r);
                this.addRoad(r);
            }

            for (int i = 0; i < ja_cars.length(); i++) {
                JSONObject obj = ja_cars.getJSONObject(i);
                Car car = null;
                switch (obj.getString("type")) {
                    case "Car":
                        car = new Car(obj);
                        break;
                    case "CarGreen":
                        car = new Car_green(obj);
                        break;
                    case "CarRed":
                        car = new Car_red(obj);
                }
                JSONArray ja_path = obj.getJSONArray("path");
                for (int j = 0; j < ja_path.length(); j++) {
                    String road_id = ja_path.getString(j);
                    Road r = roads.get(road_id);
                    car.addRoad(r);
                }
                cars.put(obj.getString("id"), car);
                this.addCar(car);
            }
            this.setName(name);
        } catch (FileNotFoundException e) {
            System.err.println("ERRORE: file non trovato");
        }
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
        if (s != null && !s.equals("null"))
            this.kb_path = s;
    }

    public Road getRoadByID(String id) {
        return roads.get(id);
    }

    public List<Road> getRoads() {
        return new ArrayList<Road>(roads.values());
    }

    public Map<String, Road> getRoadMap() {
        return roads;
    }

    public Map<String, Intersection> getIntersectionMap() {
        return intersections;
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

    public void save(String path) {
        System.err.print("Esporto JSON...");
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
