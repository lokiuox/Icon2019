package com.patterson.entity;

import com.patterson.utility.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.ImageIcon;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Intersection implements IEntity {

    private static int counter = 0;

    protected String ID;
    protected Point position = new Point(0, 0);

    //Dimensions in tiles
    protected Dimension size = new Dimension(32, 32);

    protected Set<Road> roads = new HashSet<>();
    private Set<Car> passing = new HashSet<>();
    private KnowledgeBase kb = new KnowledgeBase(); //Not initialized?

    protected Image img;

    public Intersection(String id, int x, int y, int w, int h) {
        updateCounter(id);
        ID = id;
        position.setLocation(x,y);
        size.setSize(w, h);
        loadImage();
    }

    public Intersection(JSONObject jo_intersection) {
        this(jo_intersection.getString("id"),
                jo_intersection.getInt("posX"),
                jo_intersection.getInt("posY"),
                jo_intersection.getInt("width"),
                jo_intersection.getInt("height"));
    }

    public String getType() { return "Intersection"; }

    private static void updateCounter(String id) {
        counter = Math.max(counter, Integer.parseInt(id.substring(1)));
    }

    public static String nextID() { return "i" + ++counter; }

    public Dimension getSize() {
        return new Dimension(size);
    }

    public void setSize(Dimension d) {
        size = new Dimension(d);
    }

    public Point getPosition() { return position; }

    public void setPosition(Point p) { position = new Point(p); }

    public String getID() {
        return ID;
    }

    public Set<Road> getRoads() {
        return roads;
    }

    @Override
    public void draw(Graphics2D g) {
        for (int i=0; i<size.width; i+=16) {
            for (int j=0; j<size.height; j+=16) {
                g.drawImage(img, position.x+i, position.y+j, null);
            }
        }
    }

    protected void loadImage() {
        img = new ImageIcon("resources/road/asphalt.png").getImage();
    }

    public void giveRightToPass() {

        boolean given = false;
        //System.out.println("Contest triggered");

        String assertion;
        Set<Car> contenders = new HashSet<>();  // cars needing the right of way

        // delete outdated info about contenders
        kb.clearAssertions();

        // find contenders
        if (passing.isEmpty()) {
            for (Road r : roads) {
                if (!r.getCars().isEmpty()) {
                    contenders.add(r.firstCar());
                }
            }


            // define facts about contenders
            for (Car c : contenders) {
                assertion = "strada_corrente(" + c.getID() + "," + c.getCurrentRoad().getID() + ")";
                //System.out.println(assertion);
                kb.addAssertion(assertion);
                if (c.getNextRoad() != null)
                    assertion = "prossima_strada(" + c.getID() + "," + c.getNextRoad().getID() + ")";
                kb.addAssertion(assertion);
                //System.out.println(assertion);
            }

            // add contenders facts to KB
            kb.assertToKB();

            // give the right of way to the right car
            for (Car c : contenders) {
                boolean right = kb.boolQuery("precedenza(" + c.getID() + ").");
                //System.out.println( "precedenza("+c.getID()+"): "  + right );
                if (right) {
                    c.setRightToPass();
                    given = true;
                }
            }

            if (!contenders.isEmpty() && !given) {
                contenders.iterator().next().setRightToPass();
                //System.out.println("given");
            }

            // remove contenders facts from KB
            kb.retractFromKB();
        }
    }


    public void carPassing(Car c) {
        passing.add(c);
    }

    public void carPassed(Car c) {
        passing.remove(c);
    }

    public JSONObject toJSONObject() {
        JSONObject intersection = new JSONObject();
        intersection.put("id", ID);
        intersection.put("type", getType());
        intersection.put("posX", position.x);
        intersection.put("posY", position.y);
        intersection.put("width", size.width);
        intersection.put("height", size.height);
        JSONArray ja_roads = new JSONArray();
        for (Road r: roads)
            ja_roads.put(r.getID());
        intersection.put("roads", ja_roads);
        return intersection;
    }

    public String toJSON() {
        JSONObject json_object = this.toJSONObject();
        return json_object.toString();
    }
}
