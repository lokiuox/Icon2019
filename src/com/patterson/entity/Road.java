package com.patterson.entity;

import com.patterson.utility.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class Road implements Entity {

    Point2D position = new Point2D.Float(0, 0);
    int length;
    Angle direction;

    protected Image[] img = new Image[4];

    public Road(float x, float y, int d, int l) {
        position.setLocation(x,y);
        direction = new Angle(d);
        length = l;
        loadImage();
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

    @Override
    public void draw(Graphics2D g) {
        for (int i=8; i<=length-8; i+=16) {
            if (direction.isHorizontal()) {
                g.drawImage(img[0], (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin(), null);
                g.drawImage(img[1], (int) position.getX() + i * direction.cos() - 8, (int) position.getY() + i * direction.sin() - 16, null);

            } else {
                g.drawImage(img[2], (int) position.getX() + i * direction.cos(),      (int) position.getY() + i * direction.sin() - 8, null);
                g.drawImage(img[3], (int) position.getX() + i * direction.cos() - 16, (int) position.getY() + i * direction.sin() - 8, null);
            }
        }

        g.setPaint(Color.blue);
        g.drawLine((int) position.getX(), (int) position.getY(), (int) position.getX(), (int) position.getY());
        g.drawLine((int) getEnd().getX(),
                (int) getEnd().getY(),
                (int) getEnd().getX(),
                (int) getEnd().getY());
    }

    protected void loadImage() {
        img[0] = new ImageIcon("resources/road/roadH_top.png").getImage();
        img[1] = new ImageIcon("resources/road/roadH_bottom.png").getImage();
        img[2] = new ImageIcon("resources/road/roadV_left.png").getImage();
        img[3] = new ImageIcon("resources/road/roadV_right.png").getImage();
    }
}
