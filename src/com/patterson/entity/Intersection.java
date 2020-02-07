package com.patterson.entity;

import com.patterson.utility.*;
import org.jpl7.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Intersection implements Entity {

    String ID;
    Point2D position = new Point2D.Float(0, 0);
    int width = 0;
    int height = 0;
    Set<Road> roads = new HashSet<>();
    Set<Car> passing = new HashSet<>();
    KnowledgeBase kb = new KnowledgeBase();

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
}
