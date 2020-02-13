package com.patterson.entity;

import com.patterson.utility.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.ImageIcon;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Road implements IEntity {

    private String ID;
    private Point position = new Point(0, 0);
    private int length;
    Angle direction;

    private Image[] img = new Image[4];
    private List<Car> cars = new LinkedList<>();
    private Intersection intersection = null;

    public Road(String id, int x, int y, int d, int l) {
        ID = id;
        position.setLocation(x,y);
        direction = new Angle(d);
        length = l;
        loadImage();
    }

    public Road(String id, int x, int y, int d, int l, Intersection i) {
        ID = id;
        position.setLocation(x,y);
        direction = new Angle(d);
        length = l;
        setIntersection(i);
        loadImage();
    }

    public Road(JSONObject jo_road) {
        this(jo_road.getString("id"),
                jo_road.getInt("posX"),
                jo_road.getInt("posY"),
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

    @Override
    public void draw(Graphics2D g) {
        for (int i=8; i<=length-8; i+=16) {
            if (direction.isHorizontal()) {
                g.drawImage(img[0],  position.x + i * direction.cos() - 8,  position.y + i * direction.sin(), null);
                g.drawImage(img[1],  position.x + i * direction.cos() - 8,  position.y + i * direction.sin() - 16, null);

            } else {
                g.drawImage(img[2],  position.x + i * direction.cos(),       position.y + i * direction.sin() - 8, null);
                g.drawImage(img[3],  position.x + i * direction.cos() - 16,  position.y + i * direction.sin() - 8, null);
            }
        }

        //debug
        /*g.setPaint(Color.blue);
        g.drawLine( position.x,  position.y,  position.x,  position.y);
        g.drawLine( getEnd().getX(),  getEnd().getY(),  getEnd().getX(),  getEnd().getY());
        */
    }

    protected void loadImage() {
        img[0] = new ImageIcon("resources/road/roadH_top.png").getImage();
        img[1] = new ImageIcon("resources/road/roadH_bottom.png").getImage();
        img[2] = new ImageIcon("resources/road/roadV_left.png").getImage();
        img[3] = new ImageIcon("resources/road/roadV_right.png").getImage();
    }

    public JSONObject toJSONObject() {
        JSONObject road = new JSONObject();
        road.put("id", ID);
        road.put("posX", position.x);
        road.put("posY", position.y);
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
