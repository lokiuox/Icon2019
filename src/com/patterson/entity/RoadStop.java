package com.patterson.entity;

import javax.swing.*;
import java.awt.*;

public class RoadStop extends Road {

    public RoadStop(String id, float x, float y, int d, int l) {
        super(id, x, y, d, l);
        loadStopImage();
    }

    protected void loadStopImage() {
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
                g.drawImage(img.get(0), (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin(), null);
                g.drawImage(img.get(1), (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin() - 16, null);
            } else {
                g.drawImage(img.get(2), (int) position.getX() + i * direction.cos(),      (int) position.getY() + i * direction.sin() - 8, null);
                g.drawImage(img.get(3), (int) position.getX() + i * direction.cos() - 16, (int) position.getY() + i * direction.sin() - 8, null);
            }
        }

        switch (direction.getAngle()) {
            case 0:
                g.drawImage(img.get(5), (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin(), null);
                g.drawImage(img.get(4), (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin() - 16, null);
                break;
            case 1:
                g.drawImage(img.get(7), (int) position.getX() + i * direction.cos(),      (int) position.getY() + i * direction.sin() - 8, null);
                g.drawImage(img.get(6), (int) position.getX() + i * direction.cos() - 16, (int) position.getY() + i * direction.sin() - 8, null);
                break;
            case 2:
                g.drawImage(img.get(9), (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin(), null);
                g.drawImage(img.get(8), (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin() - 32, null);
                break;
            case 3:
                g.drawImage(img.get(11), (int) position.getX() + i * direction.cos(),      (int) position.getY() + i * direction.sin() - 8, null);
                g.drawImage(img.get(10), (int) position.getX() + i * direction.cos() - 32, (int) position.getY() + i * direction.sin() - 8, null);
                break;
        }
    }
}
