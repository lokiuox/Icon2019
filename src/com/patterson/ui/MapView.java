package com.patterson.ui;

import com.patterson.world.Scenario;
import com.patterson.utility.ScenarioUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class MapView extends JPanel implements ActionListener {

    protected Timer timer = new Timer(30, this);
    protected int counter = 0;
    protected Map<String, MapControls> windowControls = new HashMap<>();

    protected Scenario scenario;

    public MapView() {
        this(new Scenario());
    }

    public MapView(String json) {
        this(new Scenario(json));
    }

    public MapView(Scenario s) {
        scenario = s;
        ScenarioUtility.setScenario(scenario);
        timer.start();
    }

    public void setControls(String id, MapControls c) {
        windowControls.put(id, c);
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