package com.patterson.world;

import com.patterson.entity.*;

import java.awt.*;
import java.util.*;

public class Actors {
    Set<Car> cars = new HashSet<>();

    public Actors(Scenario m) {
        initCars(m);
    }

    private void initCars(Scenario m) {
        Car c = new Car_red(32*2,0,0);
        c.getPath().add(m.getRoadByID("R0"));
        c.getPath().add(m.getRoadByID("R1"));
        cars.add(c);

        c = new Car_green(20*16,0,0);
        c.getPath().add(m.getRoadByID("R1"));
        c.getPath().add(m.getRoadByID("R2"));
        cars.add(c);
    }

    public void draw(Graphics2D g) {
        for (Car car : cars) car.draw(g);
    }

    public void tick() {
        for (Car car : cars) car.tick();
    }
}
