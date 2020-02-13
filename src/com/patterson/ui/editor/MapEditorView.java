package com.patterson.ui.editor;

import com.patterson.entity.Intersection;
import com.patterson.entity.Road;
import com.patterson.ui.MapView;
import com.patterson.world.Scenario;

import java.awt.*;
import java.util.*;

class MapEditorView extends MapView {

    private Map<String, IEditorMode> modes = new HashMap<>();
    private IEditorMode currentMode = null;

    private Map<String, Road> roads;
    private Map<String, Intersection> intersections;
    private MapMatrix matrix;

    public MapEditorView() {
        scenario = new Scenario();
        init();
    }

    public MapEditorView(String json) {
        scenario = new Scenario(json);
        init();
    }

    private void init() {
        initUI();
        roads = scenario.getRoadMap();
        intersections = scenario.getIntersectionMap();
        matrix = new MapMatrix(this);
        modes.put("RoadDesign", new RoadDesignMode(this));
        this.activateMode("RoadDesign");
        modes.put("TileSelect", new TileSelectMode(this));
    }

    private void initUI() {
        setFocusable(true);
    }

    public void activateMode(String id) {
        IEditorMode mode = modes.get(id);
        if (mode == null || mode == currentMode)
            return;

        if (currentMode != null) {
            currentMode.deactivate();
            currentMode = null;
        }
        currentMode = mode;
        currentMode.activate();
    }

    protected void addRoad(Road r) {
        roads.put(r.getID(), r);
        matrix.addRoad(r);
    }

    protected void addIntersection(Intersection i) {
        intersections.put(i.getID(), i);
        matrix.addIntersection(i);
    }

    protected void removeRoad(Road r) {
        roads.remove(r.getID());
        matrix.removeRoad(r);
    }

    protected void removeRoad(String id) {
        removeRoad(roads.get(id));
    }

    protected void removeIntersection(Intersection i) {
        intersections.remove(i.getID());
        matrix.removeIntersection(i);
    }

    protected void removeIntersection(String id) {
        removeIntersection(intersections.get(id));
    }

    int getRoadsSize() { return roads.size(); }

    int getIntersectionsSize() { return intersections.size(); }

    MapMatrix getMatrix() { return matrix; }

    void splitRoad(Road r, int tile_n) {
        int first_lenght = (tile_n - 1) * 32;
        matrix.removeRoad(r);
        roads.remove(r);
        Road fist_half = new Road(r.getID(), r.getPosition().x, r.getPosition().y, r.getDirection().getAngle(), first_lenght);
        Intersection i = r.getIntersection();
        addRoad(fist_half);

        if (r.getLength()/32 > tile_n) {
            int x = 0, y = 0, length;
            length = r.getLength() - first_lenght - 32;

            if (length <= 0)
                return;

            switch (r.getDirection().getAngle()) {
                case 0:
                    x = r.getPosition().x + first_lenght + 32;
                    y = r.getPosition().y;
                    break;
                case 1:
                    x = r.getPosition().x;
                    y = r.getPosition().y - first_lenght - 32;
                    break;
                case 2:
                    x = r.getPosition().x - first_lenght - 32;
                    y = r.getPosition().y;
                    break;
                case 3:
                    x = r.getPosition().x;
                    y = r.getPosition().y + first_lenght + 32;
                    break;
            }
            Road second_half = new Road(r.getID() + "bis", x, y, r.getDirection().getAngle(), length);
            second_half.setIntersection(i);
            addRoad(second_half);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Road r: roads.values())
            r.draw(g2d);
        currentMode.draw(g2d);
    }

    public static Point toGrid(int x, int y) {
        return new Point(
                16+32*(x/32),
                16+32*(y/32)
        );
    }


}
