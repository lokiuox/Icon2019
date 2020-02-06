package com.patterson.entity;

import com.patterson.utility.Angle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Queue;

public class Car implements Entity {

    String ID;
    private Point2D position = new Point2D.Float(0, 0);
    private Angle direction;
    private float speed;
    private final float maxSpeed = 8;
    private final float acceleration = 0.35f;

    protected Image[] img = new Image[4];

    Road road = null;
    Queue<Road> path = new LinkedList<>();

    public Car(String id, float x, float y, int d) {
        ID = id;
        position.setLocation(x, y);
        direction = new Angle(d);
        loadImage();
    }

    public Car(String id, float x, float y) {
        this(id, x, y, 0);
    }

    protected void loadImage() {
        img[0] = new ImageIcon("resources/car/car0_grey.png").getImage();
        img[1] = new ImageIcon("resources/car/car1_grey.png").getImage();
        img[2] = new ImageIcon("resources/car/car2_grey.png").getImage();
        img[3] = new ImageIcon("resources/car/car3_grey.png").getImage();
    }

    public void draw(Graphics2D g) {
        if (direction.isHorizontal()) {
            g.drawImage(img[direction.getAngle()], (int) position.getX() - 24, (int) position.getY() - 24, null);
        } else {
            g.drawImage(img[direction.getAngle()], (int) position.getX() - 16, (int) position.getY() - 16, null);
        }

        // debug
        /*
        g.setPaint(Color.blue);
        g.drawLine((int) position.getX(), (int) position.getY(), (int) position.getX(), (int) position.getY());

        g.drawLine((int) (position.getX() + (brakeSpace()+24)*direction.cos()), (int) (position.getY() + (brakeSpace()+16)*direction.sin()), (int) position.getX()+24*direction.cos(), (int) position.getY()+16*direction.sin());
        */
    }

    public String getID() {
        return ID;
    }

    public Queue<Road> getPath() {
        return path;
    }

    public Point2D getPosition() {
        return position;
    }

    // do stuff during the next frame
    public void tick() {

        // stop if there is no road to follow
        if (road == null) {
            setRoad(path.poll());
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
            if (isNearCar() || isRoadEnd()) {
                stop();
            } else {
                go();
            }
        }
    }

    private void setRoad(Road r) {
        if (road != null)
            road.getCars().remove(this);
        road = r;
        road.getCars().add(this);
    }

    private void roadEnd() {
        if (path.peek() != null && !isNearCar())
            setRoad(path.poll());
    }

    // accelerate
    private void go() {
        speed += acceleration;
        if (speed > maxSpeed)
            speed = maxSpeed;
        positionUpdate();
    }

    // brake
    private void stop() {
        speed -= acceleration;
        if (speed < 0)
            speed = 0;
        positionUpdate();
    }

    // apply movement
    private void positionUpdate() {
        position.setLocation(position.getX() + speed * direction.cos(), position.getY() + speed * direction.sin());
    }

    // check if the car and the road have the same direction
    private boolean hasRoadDir() {
        return direction.equals(road.getDirection());
    }

    // check if the car is on the road
    private boolean hasRoadLine() {
        boolean a = false;

        if (road.direction.isHorizontal()) {
            if (Math.abs(road.getPosition().getY() - position.getY()) <= speed) {
                position.setLocation(position.getX(), road.getPosition().getY());
                a = true;
            }
        } else {
            if (Math.abs(road.getPosition().getX() - position.getX()) <= speed) {
                position.setLocation(road.getPosition().getX(), position.getY());
                a = true;
            }
        }
        return a;
    }

    // check if the car is orthogonal to the road
    private boolean isRoadOrtho() {
        return direction.equals(road.getDirection().ortho(position, road.getPosition()));
    }

    // check if the car is near the end of the road
    private boolean isRoadEnd() {
        boolean a = false;
            if (hasRoadLine() && hasRoadDir() && isNear(road.getEnd(), 24, 20)) {
                a = true;
                roadEnd();
            }
        return a;
    }

    private boolean isNearCar() {
        boolean a = false;
        if (road != null )
            for (Car c: road.getCars()) {
                if (c!=this && isNear(new Point2D.Float((float) c.getPosition().getX(), (float) c.getPosition().getY()), 42, 32) ) {
                    a = true;
                }
            }
        return a;
    }

    // check if the car is near a point (closer than safety distance)
    public boolean isNear(Point2D p, int xOffset, int yOffset) {
        boolean a = false, b = false;

        if (direction.isHorizontal() && Math.abs(position.getY()-p.getY()) < 32 )
            b = true;
        if (direction.isVertical() && Math.abs(position.getX()-p.getX()) < 32 )
            b = true;

        switch (direction.getAngle()) {
            case 0:
                if (position.getX() + (brakeSpace()+xOffset+speed) * direction.cos() >= p.getX() && position.getX() - speed*direction.cos() <= p.getX())
                    a = true;
                break;
            case 1:
                if (position.getY() + (brakeSpace()+yOffset+speed) * direction.sin() <= p.getY() && position.getY() - speed*direction.sin() >= p.getY())
                    a = true;
                break;
            case 2:
                if (position.getX() + (brakeSpace()+xOffset+speed) * direction.cos() <= p.getX() && position.getX() - speed*direction.cos() >= p.getX())
                    a = true;
                break;
            case 3:
                if (position.getY() + (brakeSpace()+yOffset+speed) * direction.sin() >= p.getY() && position.getY() - speed*direction.sin() <= p.getY())
                    a = true;
                break;
        }

        return a && b;
    }

    // calculate how much space the car needs to completely stop
    private float brakeSpace() {
        return speed*speed / (2 * acceleration);
    }
}
