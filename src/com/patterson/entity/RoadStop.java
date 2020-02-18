package com.patterson.entity;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class RoadStop extends Road {

    public RoadStop(String id, int x, int y, int d, int l) {
        super(id, x, y, d, l);
        loadStopImage();
    }

    public RoadStop(JSONObject jo_road) {
        super(jo_road);
        loadStopImage();
    }

    @Override
    public String getType() { return "RoadStop"; }

    private void loadStopImage() {
        img.add(new ImageIcon("resources/stop/stop0_top.png").getImage());
        img.add(new ImageIcon("resources/stop/stop0_bottom.png").getImage());
        img.add(new ImageIcon("resources/stop/stop1_left.png").getImage());
        img.add(new ImageIcon("resources/stop/stop1_right.png").getImage());
        img.add(new ImageIcon("resources/stop/stop2_top.png").getImage());
        img.add(new ImageIcon("resources/stop/stop2_bottom.png").getImage());
        img.add(new ImageIcon("resources/stop/stop3_left.png").getImage());
        img.add(new ImageIcon("resources/stop/stop3_right.png").getImage());
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
                g.drawImage(img.get(5), position.x + i * direction.cos() - 8, position.y, null);
                g.drawImage(img.get(4), position.x + i * direction.cos() - 8, position.y - 16, null);
                break;
            case 1:
                g.drawImage(img.get(7), position.x,position.y + i * direction.sin() - 8, null);
                g.drawImage(img.get(6), position.x - 16, position.y + i * direction.sin() - 8, null);
                break;
            case 2:
                g.drawImage(img.get(9), position.x + i * direction.cos() - 8, position.y, null);
                g.drawImage(img.get(8), position.x + i * direction.cos() - 8, position.y - 32, null);
                break;
            case 3:
                g.drawImage(img.get(11), position.x,position.y + i * direction.sin() - 8, null);
                g.drawImage(img.get(10), position.x - 32, position.y + i * direction.sin() - 8, null);
                break;
        }

        // debug
        g.setColor(Color.WHITE);
        g.drawString(getID(), position.x, position.y);
    }
}