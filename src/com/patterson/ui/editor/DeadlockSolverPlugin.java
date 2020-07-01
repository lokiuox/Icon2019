package com.patterson.ui.editor;


import com.patterson.entity.Car;
import com.patterson.entity.Intersection;
import com.patterson.entity.Road;
import com.patterson.ui.MapView;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class DeadlockSolverPlugin {
    private MapView map;
    private int msCounter = 0;
    private int delayTicks = 100;
    private HashMap<Intersection, HashSet<Car>> intersections_incoming_cars = new HashMap<>();
    private boolean deadlock_detected = false;

    public DeadlockSolverPlugin(MapView m) {
        map = m;
        init();
    }


    public void init() {
        //screenTickMS = this.editor.getTimerDelay();
        for (Intersection i: map.getScenario().getIntersections()) {
            intersections_incoming_cars.put(i, new HashSet<>());
        }
        System.err.println("DeadLockSolver activated");
    }

    public void tick() {
        msCounter++;
        if (msCounter >= delayTicks) {
            msCounter = 0;
            checkDeadlock();
        }
    }

    private HashSet<Car> getIncomingCarSet(Intersection i) {
        HashSet<Car> cars = new HashSet<>();
        for (Road r: i.getRoads()) {
            if (r.firstCar() != null)
                cars.add(r.firstCar());
        }
        return cars;
    }

    private void checkDeadlock() {
        boolean deadlock = true;
        boolean all_empty = true;
        for (Intersection i: map.getScenario().getIntersections()) {
            HashSet<Car> incoming_cars = getIncomingCarSet(i);
            if (incoming_cars.isEmpty()) {
                continue;
            } else {
                all_empty = false;
            }
            if (!incoming_cars.equals(intersections_incoming_cars.get(i))) {
                deadlock = false;
                intersections_incoming_cars.put(i, incoming_cars);
            } else {
                System.out.println("Intersection " + i.getID() + " seems stuck");
                solveDeadlock(i);
            }
        }

        if (deadlock && !all_empty) {
            changeDestinations();/*
            if (deadlock_detected) {
                deadlock_detected = false;
                System.err.println("Deadlock detected, trying to solve...");
                changeDestinations();
            } else {
                //second chance
                System.out.println("Deadlock detected, giving second chance.");
                deadlock_detected = true;
            }*/
        }

    }

    private void changeDestinations() {
        for (Intersection i: map.getScenario().getIntersections()) {
            for (Car c: getIncomingCarSet(i)) {
                Road next = c.getNextRoad();
                if (next != null && next.isFull()) {
                    System.err.println("Assigning new dest to car " + c.getID());
                    do {
                        c.chooseNewDestination();
                    } while (c.getPath().peek() != null && !c.getPath().peek().isFull());
                }
            }
        }
    }

    private void solveDeadlock(Intersection i) {
        //System.out.println("Intersection " + i.getID());
        java.util.List<Car> possible_advancing_cars = new ArrayList<>();
        for (Car c: getIncomingCarSet(i)) {
            Road next = c.getNextRoad();
            if (next != null && !next.isFull()) {
                if (c.isNearCar()) {
                    c.disableNearCarTemporarily(20);
                    c.go();
                    continue;
                    //return;
                }
                possible_advancing_cars.add(c);
            }
        }
        if (possible_advancing_cars.size() > 0) {
            System.out.println("Found " + possible_advancing_cars.size() + " cars that can advance");
            System.out.println("Giving right of way to car " + possible_advancing_cars.get(0).getID());
            possible_advancing_cars.get(0).setRightToPass();
        } else {
            //System.out.println("No solutions found for this intersection.");
        }
    }

}
