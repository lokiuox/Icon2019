package com.patterson.world;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Surface extends JPanel implements ActionListener {

    private Timer timer;
    private int counter = 0;

    private Scenario scenario;

    public Surface() {
        scenario = new Scenario("resources/scenari/demo/scenario.json");
        timer = new Timer(30, this);
        timer.start();
    }

    public String getSceneName() {
        return scenario.getName();
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        scenario.draw(g2d);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.stop();
        scenario.tick();
        repaint();
        timer.start();
    }
}