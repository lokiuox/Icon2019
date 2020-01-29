package com.patterson.entity;

import com.patterson.utility.Angle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class Car implements Entity {

    private Point2D position = new Point2D.Float(0, 0);
    private Angle direction;
    private float speed;
    private final float maxSpeed = 8;
    private final float acceleration = 0.22f;

    protected Image[] img = new Image[4];

    Road road = null;

    public Car(float x, float y, int d) {
        position.setLocation(x,y);
        direction = new Angle(d);
        loadImage();
    }

    public Car(float x, float y) {
        this(x,y,0);
    }

    protected void loadImage() {
        img[0] = new ImageIcon("resources/car/car0_grey.png").getImage();
        img[1] = new ImageIcon("resources/car/car1_grey.png").getImage();
        img[2] = new ImageIcon("resources/car/car2_grey.png").getImage();
        img[3] = new ImageIcon("resources/car/car3_grey.png").getImage();
    }

    public void draw(Graphics2D g) {
        if (direction.isHorizontal()) {
            g.drawImage(img[direction.getAngle()], (int) position.getX()-24, (int) position.getY()-24, null);
        } else {
            g.drawImage(img[direction.getAngle()], (int) position.getX()-16, (int) position.getY()-16, null);
        }
        g.setPaint(Color.blue);
        g.drawLine((int) position.getX(), (int) position.getY(), (int) position.getX(), (int) position.getY());
    }

    // set a road for the car to follow
    public void setRoad(Road r) {
        road = r;
    }

    // accelerate
    private void go() {
        speed += acceleration;
        if (speed>maxSpeed)
            speed=maxSpeed;
        positionUpdate();
    }

    // brake
    private void stop() {
        speed -= acceleration;
        if (speed<0)
            speed=0;
        positionUpdate();
    }

    // apply movement
    private void positionUpdate() {
        position.setLocation(position.getX()+speed* direction.cos(), position.getY()+speed* direction.sin());
    }

    // do stuff for the next frame
    public void tick() {
        // stop if there is no road to follow
        if (road == null) {
            stop();
        } else {
            // if not on the road, proceed orthogonally to it
            if (!hasRoadLine() && !isRoadOrtho()) {
                direction.setAngle(road.getDirection().ortho(position, road.getPosition()));
            }

            // if on the road, follow the road
            if (hasRoadLine() && !hasRoadDir()) {
                direction.setAngle(road.getDirection());
            }

            // if near the end of the road stop, otherwise go straight on
            if (isRoadEnd()) {
                stop();
            } else {
                go();
            }
        }
    }

    // check if the car and the road have the same direction
    boolean hasRoadDir() {
        return direction.equals(road.getDirection());
    }

    // check if the car is on the road
    boolean hasRoadLine() {
        boolean a = false;

        if (road.direction.isHorizontal()) {
            if (Math.abs(road.getPosition().getY() - position.getY())<=speed) {
                position.setLocation(position.getX(),road.getPosition().getY());
                a = true;
            }
        } else {
            if (Math.abs(road.getPosition().getX() - position.getX())<=speed) {
                position.setLocation(road.getPosition().getX(),position.getY());
                a = true;
            }
        }
        return a;
    }

    // check if the car is orthogonal to the road
    boolean isRoadOrtho() {
        return direction.equals(road.getDirection().ortho(position,road.getPosition()));
    }

    // check if the car is near the end of the road
    public boolean isRoadEnd() {

        boolean a = false;

        if (hasRoadLine() && hasRoadDir()) {
            if (direction.isHorizontal()) {
                if (Math.abs(position.getX() - road.getEnd().getX()) <= brakeSpace()+24+speed)
                    a = true;
            } else if (direction.isVertical()) {
                if (Math.abs(position.getY() - road.getEnd().getY()) <= brakeSpace()+20+speed)
                    a = true;
            }
        }
        return a;
    }

    // calculate how much space the car needs to completely stop
    float brakeSpace() {
        return speed*speed/(2*acceleration);
    }
}
