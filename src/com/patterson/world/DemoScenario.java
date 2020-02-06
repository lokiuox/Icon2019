package com.patterson.world;

import com.patterson.entity.*;

import java.awt.*;
import java.util.*;

public class DemoScenario implements Scenario {

    private Set<Intersection> intersections = new HashSet<>();
    private Set<Road> roads = new HashSet<>();
    private Set<Car> cars = new HashSet<>();

    DemoScenario() {
        initScenario();
    }

    private void initScenario() {
        Car c;
        Intersection i;

        i = new Intersection("I0", 32+20*16, 4*16, 16, 16);
        intersections.add(i);

        i = new Intersection("I1", 32+20*16, 4*16+32+20*16, 16, 16);
        intersections.add(i);

        Road r0 = new Road("R0",32,5*16,0, 20*16);
        roads.add(r0);

        Road r1 = new Road("R1",32+20*16+16,5*16+16,3, 20*16);
        roads.add(r1);

        Road r2 = new Road("R2", 32+20*16, 5*16+32+20*16, 2, 10*16);
        roads.add(r2);

        Road r3 = new Road("R3", 32+20*16+16+16+10*16, 5*16, 2, 10*16);
        roads.add(r3);

        c = new Car_red("C0",32*1,0,0);
        c.getPath().add(r0);
        c.getPath().add(r1);
        c.getPath().add(r2);
        cars.add(c);

        c = new Car_green("C1",32*2,0,0);
        c.getPath().add(r0);
        c.getPath().add(r1);
        //c.getPath().add(r2);
        cars.add(c);

        c = new Car("C2",32*18,0,0);
        c.getPath().add(r3);
        c.getPath().add(r1);
        c.getPath().add(r2);
        cars.add(c);
    }

    public void draw(Graphics2D g) {
        for (Intersection i : intersections) i.draw(g);
        for (Road r : roads) r.draw(g);
        for (Car c : cars) c.draw(g);
    }

    public void tick() {
        for (Car c : cars) c.tick();
    }
}
