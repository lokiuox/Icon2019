package com.patterson.world;

import com.patterson.entity.*;
import com.patterson.utility.KnowledgeBase;

import com.patterson.utility.Packet;
import org.json.*;
import java.awt.Graphics2D;
import java.io.*;
import java.util.*;

public class Scenario {
    private String name = "New Scenario";
    private Map<String, Intersection> intersections = new LinkedHashMap<>();
    private Map<String, Road> roads = new LinkedHashMap<>();
    private Map<String, Car> cars = new LinkedHashMap<>();
    //private String kb_path = "resources/KB.pl";
    private String kb_path = "resources/KB.pl"; //solo per test
    private String json_path = null;
    private static float IEdistance = 48;

    public Scenario() {
        init();
    }

    public Scenario(String json) {
        try {
            json_path = json;
            BufferedReader reader = new BufferedReader(new FileReader(json));

            //Getting base contents
            JSONTokener jsonTokener = new JSONTokener(reader);
            JSONObject jo_scenario = new JSONObject(jsonTokener);
            JSONArray ja_roads = jo_scenario.getJSONArray("roads");
            JSONArray ja_intersections = jo_scenario.getJSONArray("intersections");
            JSONArray ja_cars = jo_scenario.getJSONArray("cars");
            String name = jo_scenario.getString("name");
            kb_path = jo_scenario.getString("kb");

            KnowledgeBase.init(kb_path);

            //Creating containing structures
            Map<String, Road> roads = new HashMap<>();
            Map<String, Intersection> intersections = new HashMap<>();
            Map<String, Car> cars = new HashMap<>();


            //Creating entities
            for (int i = 0; i < ja_intersections.length(); i++) {
                JSONObject obj = ja_intersections.getJSONObject(i);
                String intersectionType = obj.getString("type");
                Intersection intersection;
                if (intersectionType.equals("IntersectionTF")) {
                    intersection = new IntersectionTF(obj);
                } else {
                    intersection = new Intersection(obj);
                }
                intersections.put(obj.getString("id"), intersection);
                this.addIntersection(intersection);
            }

            for (int i = 0; i < ja_roads.length(); i++) {
                JSONObject obj = ja_roads.getJSONObject(i);
                String roadType = obj.getString("type");
                Road r;
                if (roadType.equals("RoadStop")) {
                    r = new RoadStop(obj);
                } else if (roadType.equals("RoadTF")) {
                    r = new RoadTF(obj);
                } else {
                    r = new Road(obj);
                }
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
                    case "CarIE":
                        car = new CarIE(obj);
                        break;
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

    }

    public void setName(String s) {
        this.name = s;
    }

    public String getName() {
        return this.name;
    }

    public String getJSONPath() { return json_path; }

    public String getKBPath() { return kb_path; }

    public void setKB(String s) {
        if (s != null && !s.equals("null"))
            this.kb_path = s;
    }

    public Road getRoadByID(String id) {
        return roads.get(id);
    }

    public List<Road> getRoads() {
        return new ArrayList<>(roads.values());
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
        return new ArrayList<>(intersections.values());
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
        return cars.get(id);
    }

    public Map<String, Car> getCarsMap() { return cars; }

    public List<Car> getCars() {
        return new ArrayList<>(cars.values());
    }

    public Scenario addCar(Car c) {
        cars.put(c.getID(), c);
        return this;
    }

    private void exchangeInformation() {
        for (Car c : cars.values())
            if (c instanceof CarIE)
                for (Car d : cars.values())
                    if (d instanceof CarIE && c.getPosition().distance(d.getPosition())<IEdistance && c!=d)
                        ((CarIE) c).sendInformation((CarIE) d);
    }

    private void updateTTL() {
        Set<Packet> packets = new HashSet<>();
        for (Car c : cars.values()) {
            if (c instanceof CarIE)
                packets.addAll(((CarIE) c).getKB().getPacketSet());
        }
        for (Packet p: packets) {
            p.tick();
        }
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
        for (Car c: cars.values())
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

    public void exportJSON(String path) {
        System.err.print("Esporto JSON... ");
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
        for (Car c : cars.values()) c.draw(g);
    }

    public float getCarAverageTime(String type) {
        float value = 0;
        int cars_total = 0;
        for (Car c: cars.values()) {
            if (c.getType().equals(type)) {
                value += c.getAverageTime();
                cars_total++;
            }
        }
        return value/cars_total;
    }

    public void tick() {
        updateTTL();
        exchangeInformation();
        for (Car c : cars.values()) c.tick();
        for (Intersection i : intersections.values())
            if (i instanceof IntersectionTF)
                ((IntersectionTF) i).tick();
    }

    public void destroy() {
        this.intersections.clear();
        this.roads.clear();
        this.cars.clear();
    }
}
