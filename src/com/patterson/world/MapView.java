package com.patterson.world;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class MapView extends JPanel implements ActionListener {

    private Timer timer;
    private int counter = 0;
    private Map<String, MapControls> controls = new HashMap<>();

    private Scenario scenario;

    public MapView() {
        scenario = new Scenario();
        timer = new Timer(30, this);
        timer.start();
    }

    public MapView(String json) {
        scenario = new Scenario(json);
        timer = new Timer(30, this);
        timer.start();
    }

    public void setControls(String type, MapControls c) {
        controls.put(type, c);
    }

    public String getSceneName() {
        return scenario.getName();
    }

    public Scenario getScenario() { return scenario; }

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

    public void pause() {
        timer.stop();
    }

    public void resume() {
        repaint();
        timer.start();
    }
}