package com.patterson.entity;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class RoadTF extends Road {

    boolean light = false;

    public RoadTF(String id, float x, float y, int d, int l) {
        super(id, x, y, d, l);
        loadTFImage();
    }

    public RoadTF(JSONObject jo_road) {
        super(jo_road);
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
    }

    public void setGreen() {
        light = true;
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
                g.drawImage(img.get(0), (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin(), null);
                g.drawImage(img.get(1), (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin() - 16, null);
            } else {
                g.drawImage(img.get(2), (int) position.getX() + i * direction.cos(),      (int) position.getY() + i * direction.sin() - 8, null);
                g.drawImage(img.get(3), (int) position.getX() + i * direction.cos() - 16, (int) position.getY() + i * direction.sin() - 8, null);
            }
        }

        switch (direction.getAngle()) {
            case 0:
                g.drawImage(img.get(light?5:6), (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin(), null);
                g.drawImage(img.get(4), (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin() - 16, null);
                break;
            case 1:
                g.drawImage(img.get(light?8:9), (int) position.getX() + i * direction.cos(),      (int) position.getY() + i * direction.sin() - 8, null);
                g.drawImage(img.get(7), (int) position.getX() + i * direction.cos() - 16, (int) position.getY() + i * direction.sin() - 8, null);
                break;
            case 2:
                g.drawImage(img.get(12), (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin(), null);
                g.drawImage(img.get(light?10:11), (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin() - 32, null);
                break;
            case 3:
                g.drawImage(img.get(15), (int) position.getX() + i * direction.cos(),      (int) position.getY() + i * direction.sin() - 8, null);
                g.drawImage(img.get(light?13:14), (int) position.getX() + i * direction.cos() - 32, (int) position.getY() + i * direction.sin() - 8, null);
                break;
        }
    }
}
