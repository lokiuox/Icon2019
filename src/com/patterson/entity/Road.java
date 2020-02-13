package com.patterson.entity;

import com.patterson.utility.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class Road implements IEntity {

    String ID;
    Point2D position = new Point2D.Float(0, 0);
    int length;
    Angle direction;

    List<Image> img = new ArrayList<>();
    List<Car> cars = new LinkedList<>();
    Intersection intersection;

    public Road(String id, float x, float y, int d, int l) {
        ID = id;
        position.setLocation(x,y);
        direction = new Angle(d);
        length = l;
        loadImage();
    }

    public Road(String id, float x, float y, int d, int l, Intersection i) {
        ID = id;
        position.setLocation(x,y);
        direction = new Angle(d);
        length = l;
        setIntersection(i);
        loadImage();
    }

    public Road(JSONObject jo_road) {
        this(jo_road.getString("id"),
                jo_road.getFloat("posX"),
                jo_road.getFloat("posY"),
                jo_road.getInt("direction"),
                jo_road.getInt("length"));
    }

    public String getID() {
        return ID;
    }

    public void setIntersection(Intersection i) {
        if (intersection!=null)
            intersection.getRoads().remove(this);

        intersection = i;
        i.getRoads().add(this);
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public Point2D getPosition() {
        return position;
    }

    public Angle getDirection() {
        return direction;
    }

    public int getLength() {
        return length;
    }

    public Point2D getEnd() {
        return new Point2D.Float((float) position.getX()+length*direction.cos(), (float) position.getY()+length*direction.sin());
    }

    public List<Car> getCars() {
        return cars;
    }

    public boolean isFull() {
        int carsize = direction.isHorizontal() ? 48 : 32;
        return cars.size() > length/carsize - 1;
    }

    @Override
    public void draw(Graphics2D g) {
        for (int i=8; i<=length-8; i+=16) {
            if (direction.isHorizontal()) {
                g.drawImage(img.get(0), (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin(), null);
                g.drawImage(img.get(1), (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin() - 16, null);

            } else {
                g.drawImage(img.get(2), (int) position.getX() + i * direction.cos(),      (int) position.getY() + i * direction.sin() - 8, null);
                g.drawImage(img.get(3), (int) position.getX() + i * direction.cos() - 16, (int) position.getY() + i * direction.sin() - 8, null);
            }
        }

        //debug
        /*g.setPaint(Color.blue);
        g.drawLine((int) position.getX(), (int) position.getY(), (int) position.getX(), (int) position.getY());
        g.drawLine((int) getEnd().getX(), (int) getEnd().getY(), (int) getEnd().getX(), (int) getEnd().getY());
        */
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
        road.put("posX", position.getX());
        road.put("posY", position.getY());
        road.put("length", length);
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
