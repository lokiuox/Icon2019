package com.patterson.entity;

import com.patterson.utility.KnowledgeBase;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class RoadTF extends Road {

    private boolean light;
    private KnowledgeBase kb = new KnowledgeBase();

    public RoadTF(String id, int x, int y, int d, int l) {
        super(id, x, y, d, l);
        loadTFImage();
        initTF();
    }

    public RoadTF(JSONObject jo_road) {
        super(jo_road);
        loadTFImage();
        initTF();
    }

    @Override
    public String getType() { return "RoadTF"; }

    private void initTF() {
        kb.addAssertion("rosso("+this.getID()+")");
    }

    private void loadTFImage() {
        img.add(new ImageIcon("resources/stop/stop0_top.png").getImage());                  //4
        img.add(new ImageIcon("resources/trafficlight/tf0_bottom_green.png").getImage());   //5
        img.add(new ImageIcon("resources/trafficlight/tf0_bottom_red.png").getImage());     //6

        img.add(new ImageIcon("resources/stop/stop1_left.png").getImage());                 //7
        img.add(new ImageIcon("resources/trafficlight/tf1_right_green.png").getImage());    //8
        img.add(new ImageIcon("resources/trafficlight/tf1_right_red.png").getImage());      //9

        img.add(new ImageIcon("resources/trafficlight/tf2_top_green.png").getImage());      //10
        img.add(new ImageIcon("resources/trafficlight/tf2_top_red.png").getImage());        //11
        img.add(new ImageIcon("resources/stop/stop2_bottom.png").getImage());               //12

        img.add(new ImageIcon("resources/trafficlight/tf3_left_green.png").getImage());     //13
        img.add(new ImageIcon("resources/trafficlight/tf3_left_red.png").getImage());       //14
        img.add(new ImageIcon("resources/stop/stop3_right.png").getImage());                //15
    }

    public void setRed() {
        light = false;
        kb.assertToKB();
    }

    public void setGreen() {
        light = true;
        kb.retractFromKB();
    }

    public boolean isRed() {
        return !light;
    }

    public boolean isGreen() {
        return light;
    }

    public void switchTF() {
        if (isGreen())
            setRed();
        else
            setGreen();
    }

    @Override
    public void draw(Graphics2D g) {
        int i;

        for (i=8; i<=length-8-16; i+=16) {
            if (direction.isHorizontal()) {
                g.drawImage(img.get(0), position.x + i * direction.cos() - 8, position.y, null);
                g.drawImage(img.get(1), position.x + i * direction.cos() - 8, position.y - 16, null);
            } else {
                g.drawImage(img.get(2), position.x,position.y + i * direction.sin() - 8, null);
                g.drawImage(img.get(3), position.x - 16, position.y + i * direction.sin() - 8, null);
            }
        }

        switch (direction.getAngle()) {
            case 0:
                g.drawImage(img.get(light?5:6), position.x + i * direction.cos() - 8, position.y, null);
                g.drawImage(img.get(4), position.x + i * direction.cos() - 8, position.y - 16, null);
                break;
            case 1:
                g.drawImage(img.get(light?8:9), position.x,position.y + i * direction.sin() - 8, null);
                g.drawImage(img.get(7), position.x - 16, position.y + i * direction.sin() - 8, null);
                break;
            case 2:
                g.drawImage(img.get(12), position.x + i * direction.cos() - 8, position.y, null);
                g.drawImage(img.get(light?10:11), position.x + i * direction.cos() - 8, position.y - 32, null);
                break;
            case 3:
                g.drawImage(img.get(15), position.x,position.y + i * direction.sin() - 8, null);
                g.drawImage(img.get(light?13:14), position.x - 32, position.y + i * direction.sin() - 8, null);
                break;
        }
        // debug
        g.setColor(Color.WHITE);
        g.drawString(getID(), position.x, position.y);
    }
}