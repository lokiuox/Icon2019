package com.patterson.world;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Surface extends JPanel implements ActionListener {

    private Timer timer;
    private int counter = 0;

    Scenario map = new Scenario();
    Actors actors = new Actors(map);

    public Surface() {
        timer = new Timer(33, this);
        timer.start();
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        map.draw(g2d);
        actors.draw(g2d);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        actors.tick();

        repaint();
    }
}