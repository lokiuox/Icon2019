package com.patterson.entity;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CarPOI extends Car {
    Map<String, PointOfInterest> pois;

    public void setPois(Map<String, PointOfInterest> map) { pois = map; }

    public CarPOI(String id, float x, float y, int d) {
        super(id, x, y, d);
    }

    public CarPOI(JSONObject jo_car) {
        super(jo_car);
    }

    public CarPOI(String id, float x, float y) {
        super(id, x, y);
    }

    @Override
    public String randomDestination() {
        int item = new Random().nextInt(pois.size());
        if (pois.isEmpty())
            return null;
        Set<Road> roads = pois.values().toArray(new PointOfInterest[0])[item].getRoads();
        if (roads.isEmpty())
            return null;
        return roads.iterator().next().getID();
    }


}
