package com.patterson;

import com.patterson.entity.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Surface extends JPanel implements ActionListener {

    private Timer timer;
    private int counter = 0;

    Car car = new Car_red(32*2,0,0);
    Road road = new Road(32,5*16,0, 20*16);
    Road road1 = new Road(32+road.getLength()+16,5*16+16,3, 20*16);

    Surface() {
        timer = new Timer(33, this);
        timer.start();

        car.setRoad(road);
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        road.draw(g2d);
        road1.draw(g2d);
        car.draw(g2d);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
        counter+=100;

        if (counter%15000<10000) {
            if (counter%15000 > 3000 && counter % 3000 == 0) {
                car.right();
            }
            car.go();
        } else {
            car.stop();
        }
        */

        car.move();
        if (car.isRoadEnd())
            car.setRoad(road1);

        repaint();
    }
}