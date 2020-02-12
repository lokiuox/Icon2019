package com.patterson.ui.editor;

import com.patterson.entity.Road;
import com.patterson.ui.MapView;

import java.awt.*;
import java.util.*;

class MapEditorView extends MapView {

    private Map<String, IEditorMode> modes = new HashMap<>();
    private IEditorMode currentMode = null;

    private Map<String, Road> roads = new HashMap<>();

    public MapEditorView() {
        initUI();
        modes.put("RoadDesign", new RoadDesignMode(this));
        this.activateMode("RoadDesign");
    }

    private void initUI() {
        setFocusable(true);
    }

    public void activateMode(String id) {
        if (currentMode != null) {
            currentMode.deactivate();
            currentMode = null;
        }
        currentMode = modes.get(id);
        currentMode.activate();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        currentMode.draw(g2d);
        for (Road r: roads.values())
            r.draw(g2d);
    }

    public static Point toGrid(int x, int y) {
        return new Point(
                32*(x/32),
                32*(y/32)
        );
    }

    protected Map<String, Road> getRoadMap() {
        return roads;
    }
}
