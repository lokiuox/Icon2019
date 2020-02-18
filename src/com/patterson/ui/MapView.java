package com.patterson.ui;

import com.patterson.world.Scenario;
import com.patterson.utility.ScenarioUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MapView extends JPanel implements ActionListener {

    protected Timer timer = new Timer(20, this);
    protected int counter = 0;
    protected Map<String, MapControls> windowControls = new HashMap<>();
    protected List<IMapPlugin> plugins = new ArrayList<>();

    protected Scenario scenario;

    public MapView() {
        this(new Scenario());
    }

    public MapView(String json) {
        this(new Scenario(json));
    }

    public MapView(Scenario s) {
        this.setScenario(s);
        init();
    }

    public void setControls(String id, MapControls c) {
        windowControls.put(id, c);
    }

    public String getSceneName() {
        return scenario.getName();
    }

    public Scenario getScenario() { return scenario; }

    public void setScenario(Scenario s) {
        scenario = s;
        ScenarioUtility.setScenario(s);
        timer.restart();
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        scenario.draw(g2d);
        for (IMapPlugin p: plugins)
            p.draw(g2d);
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
        this.requestFocus();
        timer.start();
    }

    public void pause() {
        timer.stop();
    }

    public void resume() {
        repaint();
        timer.start();
    }

    public void destroy() {
        timer.stop();
        scenario.destroy();
    }

    private void init() {
        plugins.add(new CarPathPlugin(this));
        plugins.get(0).enable();
    }
}