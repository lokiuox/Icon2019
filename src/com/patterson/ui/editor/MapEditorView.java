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
    Highlighter highlighter = new Highlighter();

    MapEditorView() {
        super();
        init();
        initUI();
    }

    MapEditorView(String json) {
        super(json);
        init();
        initUI();
    }

    MapEditorView(Scenario s) {
        super(s);
        init();
        initUI();
    }

    private void init() {
        roads = scenario.getRoadMap();
        intersections = scenario.getIntersectionMap();
        matrix = new MapMatrix(this);
    }

    @Override
    public void setScenario(Scenario s) {
        super.setScenario(s);
        init();
    }

    private void initUI() {
        setFocusable(true);
        modes.put("RoadDesign", new RoadDesignMode(this));
        modes.put("TileSelect", new InfoMode(this));
        modes.put("IntersectionDesign", new IntersectionDesignMode(this));
        modes.put("InfoMode", new InfoMode(this));
        this.activateMode("RoadDesign");
    }

    void activateMode(String id) {
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

    static Point toGrid(int x, int y) {
        return new Point(
                16+32*(x/32),
                16+32*(y/32)
        );
    }

    MapMatrix getMatrix() { return matrix; }

    int getRoadsSize() { return roads.size(); }

    int getIntersectionsSize() { return intersections.size(); }

    void addRoad(Road r) {
        if (r != null) {
            roads.put(r.getID(), r);
            matrix.addRoad(r);
        }
    }

    void addRoads(Road[] roadList) {
        for (Road r: roadList)
            addRoad(r);
    }

    void removeRoad(Road r) {
        roads.remove(r.getID());
        matrix.removeRoad(r);
        r.setIntersection(null);
    }

    void removeRoad(String id) {
        removeRoad(roads.get(id));
    }

    private boolean isPlaceable(Road r) {
        List<Point> overlappings = checkOverlappings(r);
        for (Point p: overlappings) {
            MapMatrix.TileType type = matrix.get(p.x, p.y).type;
            if (!(type == ROAD_H && r.getDirection().isVertical()) && !(type == ROAD_V && r.getDirection().isHorizontal()))
                return false;
        }
        return true;
    }

    void placeRoad(Road r) {
        if (isPlaceable(r)) {
            addAndFixOverlappings(r);
            linkIntersectionAtEndOfRoad(r);
        } else {
            System.err.println("Road not placeable here");
        }
    }

    private void addIntersection(Intersection i) {
        intersections.put(i.getID(), i);
        matrix.addIntersection(i);
    }

    void removeIntersection(Intersection i) {
        intersections.remove(i.getID());
        matrix.removeIntersection(i);
    }

    protected void removeIntersection(String id) {
        removeIntersection(intersections.get(id));
    }

    //Using matrix coords
    void placeIntersection(int x, int y) {
        if (!matrix.get(x, y).isEmpty()) {
            System.err.println("Can't put an intersection there, it's not an empty tile");
            return;
        }
        Intersection existingIntersection = null;
        MapMatrix.Tile[] neighbors = matrix.getNeighboringTiles(x, y);

        //Check if there are already intersections in the area, so we can expand that one instead of creating a new one
        if (neighbors[0].isIntersection()) {
            existingIntersection = neighbors[0].intersection;
        }
        if (neighbors[1].isIntersection()) {
            if (existingIntersection == null) {
                existingIntersection = neighbors[1].intersection;
            } else if (existingIntersection != neighbors[1].intersection) {
                System.err.println("Trying to join different interceptions. Error.");
                return;
            }
        }
        if (neighbors[2].isIntersection()) {
            if (existingIntersection == null) {
                existingIntersection = neighbors[2].intersection;
            } else if (existingIntersection != neighbors[2].intersection) {
                System.err.println("Trying to join different interceptions. Error.");
                return;
            }
        }
        if (neighbors[3].isIntersection()) {
            if (existingIntersection == null) {
                existingIntersection = neighbors[3].intersection;
            } else if (existingIntersection != neighbors[3].intersection) {
                System.err.println("Trying to join different interceptions. Error.");
                return;
            }
        }

        if (existingIntersection == null) {
            //No existing intersection is nearby, we have to create a new one
            Intersection i = new Intersection(Intersection.nextID(), x*32, y*32, 32, 32);
            addIntersection(i);
            for (Road r: getRoadsIncomingOnTile(x, y))
                r.setIntersection(i);
        } else {
            expandIntersection(existingIntersection, new Point(x, y));
        }
    }

    private void expandIntersection(Intersection i, Point pos) {
        int x = i.getPosition().x / 32;
        int y = i.getPosition().y / 32;
        int w = i.getSize().width / 32;
        int h = i.getSize().height / 32;
        if (!matrix.get(pos.x, pos.y).isEmpty())
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
            if (!matrix.get(x, pos.y).isEmpty() || !matrix.get(x+1, pos.y).isEmpty())
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
            if (!matrix.get(pos.x, y).isEmpty() || !matrix.get(pos.x, y+1).isEmpty())
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
            System.err.println("This intersection is not expandable");
        }
    }

    private void linkIntersectionAtEndOfRoad(Road r) {
        Point p = toGrid(r.getEnd().x, r.getEnd().y);
        int x = p.x/32;
        int y = p.y/32;
        int next_x = x + Math.min(0, r.getDirection().cos());
        int next_y = y + Math.min(0, r.getDirection().sin());
        MapMatrix.Tile nextTile = matrix.get(next_x, next_y);
        if (nextTile.isIntersection()) {
            r.setIntersection(nextTile.intersection);
        }
    }

    private void addAndFixOverlappings(Road r) {
        List<Road> road_segments = new ArrayList<>();
        List<Point> overlappings = checkOverlappings(r);
        Road current_segment = r;
        for (Point p: overlappings) {
            //Split already existing road
            Road existing = matrix.get(p.x, p.y).road;
            Road[] splitted_existing = splitRoad(existing, p, true);
            removeRoad(existing);
            addRoads(splitted_existing);

            //Split the road we are trying to add
            Road[] splitted_new = splitRoad(current_segment, p, true);
            if (splitted_new[0] != null)
                road_segments.add(splitted_new[0]);
            current_segment = splitted_new[1];
        }
        if (current_segment != null)
            road_segments.add(current_segment);
        //Add segments to map
        addRoads(road_segments.toArray(new Road[0]));

        //Add intersections in the empty points
        for (Point p: overlappings) {
            placeIntersection(p.x, p.y);
        }
    }

    private List<Point> checkOverlappings(Road r) {
        int x = r.getPosition().x / 32;
        int y = r.getPosition().y / 32;
        int len = r.getLength() / 32;
        List<Point> overlappings = new ArrayList<>();
        switch (r.getDirection().getAngle()) {
            case 0:
                for (int k = x; k < x+len; k++) {
                    MapMatrix.Tile t = matrix.get(k, y);
                    if (!t.isEmpty())
                        overlappings.add(new Point(k, y));
                }
                break;
            case 1:
                for (int k = y-1; k >= y-len; k--) {
                    MapMatrix.Tile t = matrix.get(x, k);
                    if (!t.isEmpty())
                        overlappings.add(new Point(x, k));
                }
                break;
            case 2:
                for (int k = x-1; k >= x-len; k--) {
                    MapMatrix.Tile t = matrix.get(k, y);
                    if (!t.isEmpty())
                        overlappings.add(new Point(k, y));
                }
                break;
            case 3:
                for (int k = y; k < y+len; k++) {
                    MapMatrix.Tile t = matrix.get(x, k);
                    if (!t.isEmpty())
                        overlappings.add(new Point(x, k));
                }
                break;
        }
        return overlappings;
    }

    private List<Road> getRoadsIncomingOnTile(int x, int y) {
        List<Road> incoming_roads = new ArrayList<>();
        MapMatrix.Tile[] neighbors = matrix.getNeighboringTiles(x, y);
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

    private Road[] splitRoad(Road r, Point pos, boolean keepSecondHalf) {
        int tile_n;
        if (r.getDirection().isHorizontal()) {
            tile_n = (pos.x - r.getPosition().x/32)*r.getDirection().cos() + Math.max(r.getDirection().cos(), 0);
        } else {
            tile_n = (pos.y - r.getPosition().y/32)*r.getDirection().sin() + Math.max(r.getDirection().sin(), 0);
        }
        return splitRoad(r, tile_n, keepSecondHalf);
    }

    private Road[] splitRoad(Road r, int tile_n, boolean keepSecondHalf) {
        Road[] splitted_road = new Road[2];
        splitted_road[0] = null;
        splitted_road[1] = null;
        int first_lenght = (tile_n - 1) * 32;

        if (first_lenght >= r.getLength()) {
            return null;
        } else if (first_lenght > 0) {
            Road fist_half = new Road(r.getID(), r.getPosition().x, r.getPosition().y, r.getDirection().getAngle(), first_lenght);
            splitted_road[0] = fist_half;
        }
        Intersection i = r.getIntersection();

        if (r.getLength()/32 > tile_n && keepSecondHalf) {
            int x, y, length;
            length = r.getLength() - first_lenght - 32;

            if (length <= 0)
                return splitted_road;

            x = r.getPosition().x + (first_lenght + 32)*r.getDirection().cos();
            y = r.getPosition().y + (first_lenght + 32)*r.getDirection().sin();

            Road second_half = new Road(Road.nextID(), x, y, r.getDirection().getAngle(), length);
            r.setIntersection(null);
            second_half.setIntersection(i);
            splitted_road[1] = second_half;
        }
        return splitted_road;
    }

    //Draw direction indicators on roads
    private static Polygon createDirectionIndicator(Road r) {
        int[] points_x = new int[3];
        int[] points_y = new int[3];
        int x = r.getPosition().x;
        int y = r.getPosition().y;
        switch (r.getDirection().getAngle()) {
            case 0:
                points_x[0] = x+6;
                points_x[1] = x+6;
                points_x[2] = x+16;
                points_y[0] = y-10;
                points_y[1] = y+10;
                points_y[2] = y;
                break;
            case 1:
                points_x[0] = x-10;
                points_x[1] = x+10;
                points_x[2] = x;
                points_y[0] = y-6;
                points_y[1] = y-6;
                points_y[2] = y-16;
                break;
            case 2:
                points_x[0] = x-6;
                points_x[1] = x-6;
                points_x[2] = x-16;
                points_y[0] = y-10;
                points_y[1] = y+10;
                points_y[2] = y;
                break;
            case 3:
                points_x[0] = x-10;
                points_x[1] = x+10;
                points_x[2] = x;
                points_y[0] = y+6;
                points_y[1] = y+6;
                points_y[2] = y+16;
                break;
        }
        return new Polygon(points_x, points_y, 3);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
        g2d.setColor(Color.WHITE);
        for (Road r: roads.values()) {
            Polygon p = createDirectionIndicator(r);
            g2d.fillPolygon(p);
        }
        g2d.setColor(Color.BLACK);
        currentMode.draw(g2d);
        highlighter.draw(g2d);
    }

    class Highlighter {
        boolean visible = false;
        Point start = new Point();
        Dimension dimensions = new Dimension();

        void set(Road r) {
            switch (r.getDirection().getAngle()) {
                case 0:
                    set(r.getPosition().x, r.getPosition().y, r.getLength(), 32);
                    break;
                case 1:
                    set(r.getPosition().x, r.getPosition().y - r.getLength(), 32, r.getLength());
                    break;
                case 2:
                    set(r.getPosition().x - r.getLength(), r.getPosition().y, r.getLength(), 32);
                    break;
                case 3:
                    set(r.getPosition().x, r.getPosition().y, 32, r.getLength());
                    break;
            }
        }

        void set(Intersection i) {
            int x = i.getPosition().x;
            int y = i.getPosition().y;
            int w = i.getSize().width;
            int h = i.getSize().height;
            set(x, y, w, h);
        }

        void set(int x, int y) {
            start = toGrid(x, y);
            start.x -= 16;
            start.y -= 16;
            dimensions.setSize(32, 32);
            visible();
        }

        void set(int xs, int ys, int w, int h) {
            start = toGrid(xs, ys);
            start.x -= 16;
            start.y -= 16;
            dimensions.setSize(w, h);
            visible();
        }

        void visible() {
            visible = true;
        }

        void invisible() {
            visible = false;
        }

        public void draw(Graphics2D g) {
            if (visible) {
                g.setColor(Color.RED);
                g.drawRect(start.x, start.y, dimensions.width, dimensions.height);
                g.setColor(new Color(255, 0, 0 , 127));
                g.fillRect(start.x, start.y, dimensions.width, dimensions.height);
                g.setColor(Color.black);
            }
        }
    }

}
