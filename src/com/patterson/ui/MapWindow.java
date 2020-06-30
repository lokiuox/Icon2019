package com.patterson.ui;

import com.patterson.world.Scenario;

import java.awt.event.WindowAdapter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.awt.*;

public class MapWindow extends JFrame {
    protected MapView mapView;
    private Map<String, MapControls> controls = new HashMap<>();
    public Dimension mapSize = new Dimension(1024, 768);
    private JPanel main;

    public MapWindow(MapView m) {
        //super(new BorderLayout());
        main = new JPanel(new BorderLayout());
        this.setContentPane(main);
        mapView = m;
        mapView.setPreferredSize(mapSize);
        mapView.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        main.add(mapView, BorderLayout.CENTER);
        main.setSize(mapSize);
        pack();
        setTitle("Traffic2D: " + mapView.getSceneName());
        //setSize(600,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new FrameClosingInterceptor());
        init();
    }

    public MapWindow(String json) {
        this(new MapView(json));
    }

    public MapWindow(Scenario s) {
        this(new MapView(s));
    }

    public void addControls(String id, MapControls c, String position) {
        controls.put(position, c);
        int w = mapSize.width;
        int h = mapSize.height;
        for (Map.Entry<String, MapControls> pair: controls.entrySet()) {
            if (pair.getKey().equals(BorderLayout.PAGE_START) || pair.getKey().equals(BorderLayout.PAGE_END))
                h += pair.getValue().getSize().height;
            else if (pair.getKey().equals(BorderLayout.LINE_START) || pair.getKey().equals(BorderLayout.LINE_END))
                w += pair.getValue().getSize().width;
        }
        main.add(c, position);
        mapView.setControls(id, c);
        main.setSize(new Dimension(w, h));
    }

    public void setMapSize(Dimension d) {
        mapSize = d;
    }

    public void setMapSize(int w, int h) {
        mapSize = new Dimension(w, h);
    }

    public MapView getMapView() {
        return mapView;
    }

    protected void init() {
        //Put your code here
    }

    private class FrameClosingInterceptor extends WindowAdapter {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            if (MainMenu.getMenu() == null) {
                windowEvent.getWindow().dispose();
                System.exit(0);
            } else {
                windowEvent.getWindow().dispose();
                mapView.getScenario().destroy();
                MainMenu.showMenu();
            }
        }
    }

}
