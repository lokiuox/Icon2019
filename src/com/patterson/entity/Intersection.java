package com.patterson.entity;

import com.patterson.utility.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

public class Intersection implements IEntity {

    String ID;
    Point2D position = new Point2D.Float(0, 0);
    int width = 0;
    int height = 0;
    Set<Road> roads = new HashSet<>();
    Set<Car> passing = new HashSet<>();
    KnowledgeBase kb = new KnowledgeBase(); //Not initialized?

    protected Image img;

    public Intersection(String id, int x, int y, int w, int h) {
        ID = id;
        position.setLocation(x,y);
        width = w;
        height = h;
        loadImage();
    }

    public String getID() {
        return ID;
    }

    public Set<Road> getRoads() {
        return roads;
    }

    @Override
    public void draw(Graphics2D g) {
        for (int i=0; i<=width; i+=16) {
            for (int j=0; j<=width; j+=16) {
                g.drawImage(img, (int)position.getX()+i, (int)position.getY()+j, null);
            }
        }
    }

    protected void loadImage() {
        img = new ImageIcon("resources/road/asphalt.png").getImage();
    }

    public void giveRightToPass() {

        System.out.println("Contest triggered");

        String assertion = "";
        Set<Car> contenders = new HashSet<>();  // cars needing the right of way

        // delete outdated info about contenders
        kb.clearAssertions();

        // find contenders
        if (passing.isEmpty()) {
            for (Road r : roads) {
                if (!r.getCars().isEmpty()) {
                    contenders.add(r.getCars().get(0));
                }
            }
        }

        // define facts about contenders
        for (Car c: contenders) {
            assertion = "strada_corrente(" + c.getID() + "," + c.getCurrentRoad().getID() + ")";
            System.out.println(assertion);
            kb.addAssertion(assertion);
            assertion = "prossima_strada(" + c.getID() + "," + c.getNextRoad().getID() + ")";
            kb.addAssertion(assertion);
            System.out.println(assertion);
        }

        // add contenders facts to KB
        kb.assertToKB();

        // give the right of way to the right car
        for (Car c: contenders) {
            boolean right = kb.boolQuery("precedenza("+c.getID()+").");
            System.out.println( "precedenza("+c.getID()+"): "  + right );
            if (right)
                c.setRightToPass();
        }

        // remove contenders facts from KB
        kb.retractFromKB();
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
        intersection.put("posX", position.getX());
        intersection.put("posY", position.getY());
        intersection.put("width", width);
        intersection.put("height", height);
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
