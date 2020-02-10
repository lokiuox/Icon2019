package com.patterson.world;

import com.patterson.entity.*;

public class DemoScenario {

    private DemoScenario() {
    }

    public static Scenario getScenario() {
        Scenario s = new Scenario();

        Intersection i0 = new Intersection("i0", 32 + 20 * 16, 4 * 16, 16, 16);
        s.addIntersection(i0);

        Intersection i1 = new Intersection("i1", 32 + 20 * 16, 4 * 16 + 32 + 20 * 16, 16, 16);
        s.addIntersection(i1);

        Road r0 = new Road("r0", 32, 5 * 16, 0, 20 * 16);
        r0.setIntersection(i0);
        s.addRoad(r0);

        Road r1 = new Road("r1", 32 + 20 * 16 + 16, 5 * 16 + 16, 3, 20 * 16);
        r1.setIntersection(i1);
        s.addRoad(r1);

        Road r2 = new Road("r2", 32 + 20 * 16, 5 * 16 + 32 + 20 * 16, 2, 10 * 16);
        s.addRoad(r2);

        Road r3 = new Road("r3", 32 + 20 * 16 + 16 + 16 + 10 * 16, 5 * 16, 2, 10 * 16);
        r3.setIntersection(i0);
        s.addRoad(r3);

        Car c1 = new Car_green("c1", 32 * 2, 0, 0);
        c1.addRoad(r0);
        c1.addRoad(r1);
        c1.addRoad(r2);
        s.addCar(c1);

        Car c2 = new Car_red("c0", 32, 0, 0);
        c2.addRoad(r0);
        c2.addRoad(r1);
        c2.addRoad(r2);
        s.addCar(c2);

        Car c3 = new Car("c2", 32 * 18, 0, 0);
        c3.addRoad(r3);
        c3.addRoad(r1);
        c3.addRoad(r2);
        s.addCar(c3);
        s.setName("DemoScenario");
        return s;
    }
}
