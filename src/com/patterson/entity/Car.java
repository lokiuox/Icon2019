package com.patterson.entity;

import com.patterson.utility.Angle;
import com.patterson.utility.KnowledgeBase;
import com.patterson.utility.ScenarioUtility;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.plugin.javascript.navig4.Link;

import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class Car implements IEntity {

    private static int counter = 0;

    private String ID;
    private Point2D position = new Point2D.Float(0, 0);
    private Angle direction;
    protected float speed;
    //private final float maxSpeed = 8;
    private final float acceleration = 0.35f;
    protected INavigator navigator = new NavigatorAStar();

    protected Image[] img = new Image[4];

    protected Road road = null;
    protected Road previousRoad = null;
    protected String destination;

    protected boolean passing = false;
    protected boolean rightToPass = false;

    protected Queue<Road> path = new LinkedList<>();

    public Car(String id, float x, float y, int d) {
        updateCounter(id);
        ID = id;
        position.setLocation(x, y);
        direction = new Angle(d);
        loadImage();
    }

    public Car(String id, float x, float y) {
        this(id, x, y, 0);
    }

    public Car(JSONObject jo_car) {
        this(jo_car.getString("id"),
                jo_car.getFloat("posX"),
                jo_car.getFloat("posY"),
                jo_car.getInt("direction"));
    }

    private static void updateCounter(String id) {
        counter = Math.max(counter, Integer.parseInt(id.substring(1)));
    }

    public static String nextID() { return "c" + ++counter; }

    public String getType() { return "Car"; }

    protected void loadImage() {
        img[0] = new ImageIcon("resources/car/car0_green.png").getImage();
        img[1] = new ImageIcon("resources/car/car1_green.png").getImage();
        img[2] = new ImageIcon("resources/car/car2_green.png").getImage();
        img[3] = new ImageIcon("resources/car/car3_green.png").getImage();
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
        // debug
        g.setColor(Color.WHITE);
        g.drawString(getID(), (int) position.getX(), (int) position.getY());
    }

    public String getID() {
        return ID;
    }

    public Queue<Road> getPath() {
        return path;
    }

    public void setPath(List<String> l) {
        path.clear();
        for (String r:l) {
            path.add(ScenarioUtility.getRoadByID(r));
        }
    }

    public Point2D getPosition() {
        return position;
    }

    public Road getCurrentRoad() {
        return road;
    }

    public Road getPreviousRoad() {
        return previousRoad;
    }

    public Road getNextRoad() {
        return path.peek();
    }

    public void setNavigator(INavigator n) {
        navigator = n;
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

            if (passing && hasReachedRoad()) {
                previousRoad.getIntersection().carPassed(this);
                passing = false;
            }

            // if near the end of the road stop, otherwise go straight on
            if (isNearCar() || isRoadEnd()) {
                stop();
            } else {
                go();
            }
        }
    }

    protected void calculatePath() {
        setPath(navigator.calculatePath(road.getID(), destination));
    }

    protected boolean greenTF() {
        return !(road instanceof RoadTF) || ((RoadTF) road).isGreen();
    }

    protected void roadEnd() {
        // if the path has been completed, create a new path to a random destination
        int i = 0;
        if (path.peek() == null && navigator != null) {
            do {
                do {
                    destination = randomDestination();
                } while (destination.equals(road.getID()));
                calculatePath();
                i++;
            } while (isUTurnPath() && i<10);
        }

        // check right of way
        if (path.peek() != null && !isNearCar() && greenTF() && !getNextRoad().isFull() ) {
            if (rightToPass || road.getIntersection()==null) {
                rightToPass = false;
                road.getIntersection().carPassing(this);
                passing = true;
                setRoad(path.poll());
            } else {
                road.getIntersection().giveRightToPass();
            }
        }
    }

    private void setRoad(Road r) {
        if (road != null)
            road.getCars().remove(this);
        previousRoad = road;
        road = r;
        road.getCars().add(this);
    }

    public void setRightToPass() {
        rightToPass = true;
    }

    // accelerate
    private void go() {
        speed += acceleration;
        if (speed > road.getMaxSpeed())
            speed = road.getMaxSpeed();
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

    // check if the car is aligned with the road
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

    // check if the car has reached his road
    private boolean hasReachedRoad() {
        //return /*isNear(road.getPosition(),0,0) &&*/ hasRoadDir();
        boolean b = false;

        switch (direction.getAngle()) {
            case 0:
                if (position.getX() + (brakeSpace()+24+speed) * direction.cos() >= road.getPosition().getX())
                    b = true;
                break;
            case 1:
                if (position.getY() + (brakeSpace()+20+speed) * direction.sin() <= road.getPosition().getY())
                    b = true;
                break;
            case 2:
                if (position.getX() + (brakeSpace()+24+speed) * direction.cos() <= road.getPosition().getX())
                    b = true;
                break;
            case 3:
                if (position.getY() + (brakeSpace()+20+speed) * direction.sin() >= road.getPosition().getY())
                    b = true;
                break;
        }

        return b && hasRoadDir();
    }

    // check if the car is orthogonal to the road
    private boolean isRoadOrtho() {
        return direction.equals(road.getDirection().ortho(position, road.getPosition()));
    }

    // check if the car is near the end of the road
    private boolean isRoadEnd() {
        boolean a = false;
            //if (hasRoadLine() && hasRoadDir() && isNear(road.getEnd(), 24, 20)) {
            //    a = true;
            //    roadEnd();
            //}

        switch (direction.getAngle()) {
            case 0:
                if (position.getX() + (brakeSpace()+24+speed) * direction.cos() >= road.getEnd().getX())
                    a = true;
                break;
            case 1:
                if (position.getY() + (brakeSpace()+20+speed) * direction.sin() <= road.getEnd().getY())
                    a = true;
                break;
            case 2:
                if (position.getX() + (brakeSpace()+24+speed) * direction.cos() <= road.getEnd().getX())
                    a = true;
                break;
            case 3:
                if (position.getY() + (brakeSpace()+20+speed) * direction.sin() >= road.getEnd().getY())
                    a = true;
                break;
        }

        if (a) {
            a = hasRoadLine() && hasRoadDir();
        }
        if (a)
            roadEnd();

        return a;
    }

    protected boolean isNearCar() {
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

    public void addRoad(Road r) {
        this.path.add(r);
    }

    public void addRoads(List<Road> list) {
        path.addAll(list);
    }

    public String randomDestination() {
        Set<Map<String, String>> roadSet = KnowledgeBase.stringQuery("strada(X).");

        int size = roadSet.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for(Map<String, String> r : roadSet)
        {
            if (i == item)
                return r.get("X");
            i++;
        }
        return null;
    }

    public JSONObject toJSONObject() {
        JSONObject car = new JSONObject();
        car.put("id", ID);
        car.put("posX", position.getX());
        car.put("posY", position.getY());
        car.put("direction", direction.getAngle());
        car.put("type", getType());
        JSONArray ja_path = new JSONArray();
        for (Road r: new ArrayList<>(path))
            ja_path.put(r.getID());
        car.put("path", ja_path);
        return car;
    }

    public String toJSON() {
        JSONObject json_object = this.toJSONObject();
        return json_object.toString();
    }

    boolean isUTurn(Road r1, Road r2) {
        boolean b = false;

        if (r1.getDirection().isHorizontal() && r2.getDirection().isHorizontal() || r1.getDirection().isVertical() && r2.getDirection().isVertical())
            if (r1.getDirection().getAngle()!=r2.getDirection().getAngle())
                b = true;

        return b;
    }

    boolean isUTurnPath() {
        boolean b = false;

        LinkedList<Road> list = new LinkedList<>(path);
        list.addFirst(road);

        for (int i=1; i<list.size(); i++)
            if (isUTurn(list.get(i-1),list.get(i)))
                b = true;

        return b;
    }
}
