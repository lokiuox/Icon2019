package com.patterson.ui;

import com.patterson.entity.Car;
import com.patterson.entity.Road;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;


public class CarPathPlugin implements IMapPlugin {
    private static Image marker_img = new ImageIcon("resources/car/marker.png").getImage();
    private MapView map;
    Map<String, Car> cars;
    private Car selectedCar = null;
    private int selectedCarIndex = 0;
    private PressAdapter pa = new PressAdapter();
    private Road current_road = null;
    private Point destination = new Point();

    CarPathPlugin(MapView m) {
        setMapView(m);
        init();
    }

    private void repaint() {
        map.repaint();
    }

    @Override
    public void init() {
        cars = map.getScenario().getCarsMap();
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (selectedCar != null) {
            drawCarPath(selectedCar, g2d);
        }

    }

    @Override
    public void setMapView(MapView m) {
        this.map = m;
    }

    @Override
    public void enable() {
        map.addKeyListener(pa);
    }

    @Override
    public void disable() {
        map.removeKeyListener(pa);
    }

    private void drawCarPath(Car c, Graphics2D g) {
        List<Integer> x_points = new ArrayList<>();
        List<Integer> y_points = new ArrayList<>();
        LinkedList<Road> carPath = new LinkedList<>(c.getPath());
        //if (carPath.isEmpty()) return;
        Point[] coords;
        if (current_road == null) current_road = c.getCurrentRoad();
        if (c.getCurrentRoad().getDirection().getAngle() == current_road.getDirection().getAngle())
        {
            coords = getLineCoords(c.getCurrentRoad());
            x_points.add((int)c.getPosition().getX());
            y_points.add((int)c.getPosition().getY());
            x_points.add(coords[1].x);
            y_points.add(coords[1].y);
        } else if (c.getCurrentRoad().getDirection().getAngle() == c.getDirection().getAngle()) {
            current_road = c.getCurrentRoad();
            coords = getLineCoords(c.getCurrentRoad());
            x_points.add((int)c.getPosition().getX());
            y_points.add((int)c.getPosition().getY());
            x_points.add(coords[1].x);
            y_points.add(coords[1].y);
        } else if (c.getDirection().getAngle() == current_road.getDirection().getAngle()){
            Point j = getJoinCoords(current_road, c.getCurrentRoad());
            coords = getLineCoords(c.getCurrentRoad());
            x_points.add((int)c.getPosition().getX());
            y_points.add((int)c.getPosition().getY());
            x_points.add(j.x);
            y_points.add(j.y);
            x_points.add(coords[1].x);
            y_points.add(coords[1].y);
        } else {
            coords = getLineCoords(c.getCurrentRoad());
            x_points.add((int)c.getPosition().getX());
            y_points.add((int)c.getPosition().getY());
            x_points.add(coords[0].x);
            y_points.add(coords[0].y);
            x_points.add(coords[1].x);
            y_points.add(coords[1].y);
        }
        Road last = c.getCurrentRoad();
        for (Road r: carPath) {
            Point join = getJoinCoords(last, r);

            if (join != null) {
                x_points.add(join.x);
                y_points.add(join.y);
            }

            coords = getLineCoords(r);
            //g.drawLine(coords[0].x, coords[0].y, coords[1].x, coords[1].y);
            x_points.add(coords[0].x);
            y_points.add(coords[0].y);
            x_points.add(coords[1].x);
            y_points.add(coords[1].y);
            last = r;
        }
        setMarkerPosition(last);
        x_points.remove(x_points.size()-1);
        y_points.remove(y_points.size()-1);
        if (last.getDirection().isHorizontal()) {
            x_points.add(destination.x + 16);
            y_points.add(destination.y + 32);
        } else {
            x_points.add(destination.x + 16);
            y_points.add(destination.y + 32);
        }

        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawPolyline(x_points.stream().mapToInt(i->i).toArray(), y_points.stream().mapToInt(i->i).toArray(), x_points.size());
        drawMapMarker(g);
    }

    private void drawMapMarker(Graphics2D g) {
        g.drawImage(marker_img, destination.x, destination.y,null);
    }

    private void setMarkerPosition(Road r) {
        Point p = new Point();
        if (r == null && destination == null) {
            return;
        } else if (r == null) {
            p = destination;
        } else {
            if (r.getDirection().isHorizontal()) {
                p.x = r.getPosition().x + (r.getLength() / 2) * r.getDirection().cos();
                p.y = r.getPosition().y - 32;
            } else {
                p.y = r.getPosition().y + (r.getLength() / 2) * r.getDirection().sin();
                p.x = r.getPosition().x - 16;
            }
            destination = p;
        }
    }

    private Point[] getLineCoords(Road r) {
        Point start;
        Point end = new Point();
        if (r.getDirection().isHorizontal()) {
            start = r.getPosition();
            end.x = start.x + r.getLength() * r.getDirection().cos();
            end.y = start.y;
        } else {
            start = r.getPosition();
            end.y = start.y + r.getLength() * r.getDirection().sin();
            end.x = start.x;
        }
        return new Point[] {start, end};
    }

    private Point getJoinCoords(Road from, Road to) {
        Point start_coords = getLineCoords(from)[1];
        Point end_coords = getLineCoords(to)[0];
        Point join_coords = new Point();
        int angle = from.getDirection().getAngle() - to.getDirection().getAngle();
        angle = (angle == -3 ? 1 : (angle == 3 ? -1 : angle));
        if (angle == 0) {
            return null;
        } else if (from.getDirection().isHorizontal()) {
            join_coords.x = end_coords.x;
            join_coords.y = start_coords.y;
        } else if (from.getDirection().isVertical()) {
            join_coords.x = start_coords.x;
            join_coords.y = end_coords.y;
        }
        return join_coords;
    }

    class PressAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    selectedCarIndex = (selectedCarIndex == 0 ? cars.size()-1 : selectedCarIndex-1);
                    selectedCar = new LinkedList<>(cars.values()).get(selectedCarIndex);
                    break;
                case KeyEvent.VK_DOWN:
                    selectedCarIndex = (selectedCarIndex+1) % cars.size();
                    selectedCar = new LinkedList<>(cars.values()).get(selectedCarIndex);
                    break;
                case KeyEvent.VK_D:
                    selectedCar = null;
                    break;
                case KeyEvent.VK_T:
                    float car_time = map.getScenario().getCarAverageTime("Car");
                    float carie_time = map.getScenario().getCarAverageTime("CarIE");
                    System.out.println("Average time to complete a path:");
                    System.out.println("Normal Car: " + car_time);
                    System.out.println("Info Sharing Car: " + carie_time);
            }
            repaint();
        }
    }
}
