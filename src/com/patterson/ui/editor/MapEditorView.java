package com.patterson.ui.editor;

import com.patterson.entity.Intersection;
import com.patterson.entity.Road;
import com.patterson.ui.MapView;
import com.patterson.world.Scenario;

import java.awt.*;
import java.util.*;
import java.util.List;

import static com.patterson.ui.editor.MapMatrix.TileType.*;

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
        Point p = checkOverlapping(r);
        if (p==null) {
            roads.put(r.getID(), r);
            matrix.addRoad(r);
        } else {
            System.out.println("Overlaps in " + p.x + "," + p.y);
        }
    }

    protected void addIntersection(Intersection i) {
        intersections.put(i.getID(), i);
        matrix.addIntersection(i);
    }

    protected void removeRoad(Road r) {
        roads.remove(r.getID());
        matrix.removeRoad(r);
        r.setIntersection(null);
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

    List<Road> splitRoad(Road r, int tile_n, boolean keepSecondHalf) {
        List<Road> splitted_road = new ArrayList<>(2);
        int first_lenght = (tile_n - 1) * 32;

        if (first_lenght >= r.getLength()) {
            splitted_road.add(r);
            return splitted_road;
        }

        Road fist_half = new Road(r.getID(), r.getPosition().x, r.getPosition().y, r.getDirection().getAngle(), first_lenght);
        Intersection i = r.getIntersection();
        splitted_road.add(fist_half);

        if (r.getLength()/32 > tile_n && keepSecondHalf) {
            int x = 0, y = 0, length;
            length = r.getLength() - first_lenght - 32;

            if (length <= 0)
                return splitted_road;

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
            splitted_road.add(second_half);
        }
        return splitted_road;
    }

    private Point checkOverlapping(Road r) {
        int x = r.getPosition().x;
        int y = r.getPosition().y;
        int len = r.getLength();
        switch (r.getDirection().getAngle()) {
            case 0:
                for (int k = x; k < x+len; k++) {
                    MapMatrix.Tile t = matrix.getCoords(k, y);
                    if (t.type != MapMatrix.TileType.EMPTY)
                        return new Point(k, y);
                }
                break;
            case 1:
                for (int k = y-len; k < y; k++) {
                    MapMatrix.Tile t = matrix.getCoords(x, k);
                    if (t.type != MapMatrix.TileType.EMPTY)
                        return new Point(x, k);
                }
                break;
            case 2:
                for (int k = x-len; k < x; k++) {
                    MapMatrix.Tile t = matrix.getCoords(k, y);
                    if (t.type != MapMatrix.TileType.EMPTY)
                        return new Point(k, y);
                }
                break;
            case 3:
                for (int k = y; k < y+len; k++) {
                    MapMatrix.Tile t = matrix.getCoords(x, k);
                    if (t.type != MapMatrix.TileType.EMPTY)
                        return new Point(x, k);
                }
                break;
        }
        return null;
    }

    private List<Road> getRoadsIncomingOnTile(int x, int y) {
        List<Road> incoming_roads = new ArrayList<>();
        MapMatrix.Tile[] neighbors = matrix.getNeighboringTilesFromCoords(x, y);
        if (neighbors[0].type == ROAD_H && neighbors[0].road.getDirection().getAngle() == 0) {
            incoming_roads.add(neighbors[0].road);
        }
        if (neighbors[1].type == ROAD_V && neighbors[1].road.getDirection().getAngle() == 1) {
            incoming_roads.add(neighbors[1].road);
        }
        if (neighbors[2].type == ROAD_H && neighbors[2].road.getDirection().getAngle() == 2) {
            incoming_roads.add(neighbors[2].road);
        }
        if (neighbors[3].type == ROAD_V && neighbors[3].road.getDirection().getAngle() == 3) {
            incoming_roads.add(neighbors[3].road);
        }
        return incoming_roads;
    }

    //Using matrix coords
    void placeIntersection(int x, int y) {
        System.out.println("place called " + x + "," + y);
        if (matrix.getCoords(x, y).type != MapMatrix.TileType.EMPTY) {
            System.err.println("Can't put an intersection there, it's not an empty tile");
            return;
        }
        Intersection existingIntersection = null;
        MapMatrix.Tile[] neighbors = matrix.getNeighboringTiles(x, y);

        //Check if there are already intersections in the area, so we can expand that one instead of creating a new one
        if (neighbors[0].type == INTERSECTION) {
            existingIntersection = neighbors[0].intersection;
        }
        if (neighbors[1].type == INTERSECTION) {
            if (existingIntersection == null) {
                existingIntersection = neighbors[1].intersection;
            } else if (existingIntersection != neighbors[1].intersection) {
                System.err.println("Trying to join different interceptions. Error.");
                return;
            }
        }
        if (neighbors[2].type == INTERSECTION) {
            if (existingIntersection == null) {
                existingIntersection = neighbors[2].intersection;
            } else if (existingIntersection != neighbors[2].intersection) {
                System.err.println("Trying to join different interceptions. Error.");
                return;
            }
        }
        if (neighbors[3].type == INTERSECTION) {
            if (existingIntersection == null) {
                existingIntersection = neighbors[3].intersection;
            } else if (existingIntersection != neighbors[3].intersection) {
                System.err.println("Trying to join different interceptions. Error.");
                return;
            }
        }

        if (existingIntersection == null) {
            //No existing intersection is nearby, we have to create a new one
            Intersection i = new Intersection("i" + intersections.size(), x*32, y*32, 32, 32);
            addIntersection(i);
            for (Road r: getRoadsIncomingOnTile(x, y))
                r.setIntersection(i);
        } else {
            expandIntersection(existingIntersection, new Point(x, y));
        }
    }

    public boolean assertEmptyTile(int x, int y) {
        return (matrix.get(x, y).type == EMPTY);
    }

    private void expandIntersection(Intersection i, Point pos) {
        System.out.println("expand called");
        int x = i.getPosition().x / 32;
        int y = i.getPosition().y / 32;
        int w = i.getSize().width / 32;
        int h = i.getSize().height / 32;
        if (!assertEmptyTile(pos.x, pos.y))
            return;
        if (w == 1 && h == 1) {
            if (pos.x == x && pos.y == y+1) {
                i.setSize(new Dimension(w*32, (h+1)*32));
            } else if (pos.x == x && pos.y == y-1) {
                i.setSize(new Dimension(w*32, (h+1)*32));
                i.setPosition(new Point(i.getPosition().x, i.getPosition().y-32));
            } else if (pos.x == x+1 && pos.y == y) {
                i.setSize(new Dimension((w+1)*32, h*32));
            } else if (pos.x == x-1 && pos.y == y) {
                i.setSize(new Dimension((w+1)*32, h*32));
                i.setPosition(new Point(i.getPosition().x-32, i.getPosition().y));
            } else {
                System.err.println("Cannot expand intersections this way. Error.");
                return;
            }
            matrix.set(pos.x, pos.y, matrix.new Tile(i));
            for (Road r: getRoadsIncomingOnTile(pos.x, pos.y))
                r.setIntersection(i);
        } else if (w == 2 && h == 1) {
            if (!assertEmptyTile(x, pos.y) || !assertEmptyTile(x+1, pos.y))
                return;
            if (pos.y == y+1 && (pos.x == x || pos.x == x+1)) {
                i.setSize(new Dimension(w*32, (h+1)*32));
            } else if (pos.y == y-1 && (pos.x == x || pos.x == x+1)) {
                i.setPosition(new Point(i.getPosition().x, i.getPosition().y-32));
                i.setSize(new Dimension(w*32, (h+1)*32));
            } else {
                System.err.println("Cannot expand intersections this way. Error.");
                return;
            }
            matrix.set(x, pos.y, matrix.new Tile(i));
            for (Road r: getRoadsIncomingOnTile(x, pos.y))
                r.setIntersection(i);
            matrix.set(x+1, pos.y, matrix.new Tile(i));
            for (Road r: getRoadsIncomingOnTile(x+1, pos.y))
                r.setIntersection(i);
        } else if (w == 1 && h == 2) {
            if (!assertEmptyTile(pos.x, y) || !assertEmptyTile(pos.x, y+1))
                return;
            if (pos.x == x+1 && (pos.y == y || pos.y == y+1)) {
                i.setSize(new Dimension((w+1)*32, h*32));
            } else if (pos.x == x-1 && (pos.y == y || pos.y == y+1)) {
                i.setPosition(new Point(i.getPosition().x-32, i.getPosition().y));
                i.setSize(new Dimension((w+1)*32, h*32));
            } else {
                System.err.println("Cannot expand intersections this way. Error.");
                return;
            }
            matrix.set(pos.x, y, matrix.new Tile(i));
            for (Road r: getRoadsIncomingOnTile(pos.x, y))
                r.setIntersection(i);
            matrix.set(pos.x, y+1, matrix.new Tile(i));
            for (Road r: getRoadsIncomingOnTile(pos.x, y+1))
                r.setIntersection(i);
        } else {
            System.err.println("Stop. You can't do that.");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
        currentMode.draw(g2d);
    }

    public static Point toGrid(int x, int y) {
        return new Point(
                16+32*(x/32),
                16+32*(y/32)
        );
    }


}
