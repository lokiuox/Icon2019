package com.patterson.ui.editor;

import com.patterson.entity.*;
import com.patterson.ui.MapView;

import java.util.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.patterson.ui.editor.MapEditorView.toGrid;


public class AutoSpawnerPlugin implements IEditorPlugin {
    private MapEditorView editor;
    private java.util.List<Car> cars = new ArrayList<>();
    private int maxCars = 20;
    private double normalCarProbability = 0.5;
    private int delayMS = 500;
    private int screenTickMS;
    private int msCounter = 0;
    private int carsToSpawn = 2;
    private boolean carsDisappearOnGoal = true;
    private boolean enabled = false;
    private Timer timer;


    AutoSpawnerPlugin(MapEditorView m) {
        this.setEditor(m);
        init();
    }

    public void setDelayMS(int delay) {
        this.delayMS = delay;
        screenTickMS = this.editor.getTimerDelay();
    }

    @Override
    public void setEditor(MapEditorView m) {
        this.editor = m;
    }

    @Override
    public void init() {
        //this.timer = new Timer(delayMS, e -> spawnCar());
        //this.timer.setRepeats(true);
        screenTickMS = this.editor.getTimerDelay();
    }

    @Override
    public void draw(Graphics2D g2d) {
        msCounter += screenTickMS;
        if (msCounter >= delayMS) {
            msCounter %= delayMS;
            spawnCar();
        }
    }

    @Override
    public void setMapView(MapView m) {
        this.editor = (MapEditorView) m;
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }

    private void spawnCar() {
        Road road = selectRandomRoad();
        //System.out.println(road.getID());

        // Create the car
        CarPOI c;
        Point pos = toGrid(road.getPosition().x, road.getPosition().y);
        // Select car type based on probability
        double random = ThreadLocalRandom.current().nextDouble();
        if (random <= normalCarProbability) {
            c = new CarPOI(Car.nextID(), pos.x, pos.y, road.getDirection().getAngle());
        } else {
            c = new CarIEPOI(Car.nextID(), pos.x, pos.y, road.getDirection().getAngle());
        }
        c.addRoad(road);
        c.setPois(editor.getScenario().getPoiMap());
        editor.addCar(c);
    }

    private Road selectRandomRoad() {
        List<Road> roadList = this.editor.getScenario().getRoads();
        int randomNum = ThreadLocalRandom.current().nextInt(0, roadList.size());
        return roadList.get(randomNum);
    }
}
