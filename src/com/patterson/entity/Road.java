package com.patterson.entity;

import com.patterson.utility.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.ImageIcon;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Road implements IEntity {

    private static int counter = 0;

    protected String ID;
    protected Point position = new Point(0, 0);
    protected int length;
    protected Angle direction;
    protected int maxSpeed = 8;

    protected List<Image> img = new ArrayList<>();
    private List<Car> cars = new LinkedList<>();
    private Intersection intersection = null;

    public Road(String id, int x, int y, int d, int l) {
        updateCounter(id);
        ID = id;
        position.setLocation(x,y);
        direction = new Angle(d);
        length = l;
        loadImage();
    }

    public Road(JSONObject jo_road) {
        this(jo_road.getString("id"),
                jo_road.getInt("posX"),
                jo_road.getInt("posY"),
                jo_road.getInt("direction"),
                jo_road.getInt("length"));
        this.setMaxSpeed(jo_road.getInt("maxspeed"));
    }

    public String getType() { return "Road"; }

    private static void updateCounter(String id) {
        counter = Math.max(counter, Integer.parseInt(id.substring(1)));
    }

    public static String nextID() { return "r" + ++counter; }

    public String getID() {
        return ID;
    }

    public void setIntersection(Intersection i) {
        if (intersection!=null)
            intersection.getRoads().remove(this);

        intersection = i;
        if (intersection!=null)
            intersection.getRoads().add(this);
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public Point getPosition() {
        return position;
    }

    public Angle getDirection() {
        return direction;
    }

    public int getLength() {
        return length;
    }

    public Point getEnd() {
        return new Point(position.x+length*direction.cos(),position.y+length*direction.sin());
    }

    public List<Car> getCars() {
        return cars;
    }

    public boolean isFull() {
        int carsize = direction.isHorizontal() ? 48 : 32;
        return cars.size() > length/carsize - 1;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int s) {
        maxSpeed = s;
    }

    @Override
    public void draw(Graphics2D g) {
        for (int i=8; i<=length-8; i+=16) {
            if (direction.isHorizontal()) {
                g.drawImage(img.get(0),  position.x + i * direction.cos() - 8,  position.y, null);
                g.drawImage(img.get(1),  position.x + i * direction.cos() - 8,  position.y - 16, null);
            } else {
                g.drawImage(img.get(2),  position.x,       position.y + i * direction.sin() - 8, null);
                g.drawImage(img.get(3),  position.x - 16,  position.y + i * direction.sin() - 8, null);
            }
        }

        // debug
        g.setColor(Color.WHITE);
        g.drawString(getID(), position.x, position.y);
}

    protected void loadImage() {
        img.add(new ImageIcon("resources/road/roadH_top.png").getImage());
        img.add(new ImageIcon("resources/road/roadH_bottom.png").getImage());
        img.add(new ImageIcon("resources/road/roadV_left.png").getImage());
        img.add(new ImageIcon("resources/road/roadV_right.png").getImage());
    }

    public JSONObject toJSONObject() {
        JSONObject road = new JSONObject();
        road.put("id", ID);
        road.put("type", getType());
        road.put("posX", position.x);
        road.put("posY", position.y);
        road.put("length", length);
        road.put("maxspeed", getMaxSpeed());
        road.put("direction", direction.getAngle());
        if (intersection != null)
            road.put("intersection", intersection.getID());
        else
            road.put("intersection", "null");
        JSONArray ja_cars = new JSONArray();
        for (Car c: cars)
            ja_cars.put(c.getID());
        road.put("cars", ja_cars);
        return road;
    }

    public String toJSON() {
        JSONObject json_object = this.toJSONObject();
        return json_object.toString();
    }
}
