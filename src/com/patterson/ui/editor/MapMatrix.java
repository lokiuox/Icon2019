package com.patterson.ui.editor;

import com.patterson.entity.Intersection;
import com.patterson.entity.Road;

import java.util.*;
import java.awt.*;

class MapMatrix {
    public enum TileType {EMPTY, ROAD_H, ROAD_V, INTERSECTION}
    private static int TILESIZE = 32;

    private MapEditorView editor;
    private Map<String, Road> roads;
    private Map<String, Intersection> intersections;
    private Map<Point, Tile> matrix = new HashMap<>();

    MapMatrix(MapEditorView m) {
        this.editor = m;
        this.roads = editor.getScenario().getRoadMap();
        this.intersections = editor.getScenario().getIntersectionMap();
        initMatrix();
    }

    private void initMatrix() {
        for (Road r: roads.values())
            addRoad(r);
        for (Intersection i: intersections.values())
            addIntersection(i);
    }

    void addRoad(Road r) {
        Point pos = r.getPosition();
        int x = pos.x / TILESIZE;
        int y = pos.y / TILESIZE;
        int len = r.getLength() / TILESIZE;
        switch (r.getDirection().getAngle()) {
            case 0:
                for (int k = x; k < x+len; k++) {
                    matrix.put(new Point(k, y), new Tile(r));
                }
                break;
            case 1:
                for (int k = y-len; k < y; k++) {
                    matrix.put(new Point(x, k), new Tile(r));
                }
                break;
            case 2:
                for (int k = x-len; k < x; k++) {
                    matrix.put(new Point(k, y), new Tile(r));
                }
                break;
            case 3:
                for (int k = y; k < y+len; k++) {
                    matrix.put(new Point(x, k), new Tile(r));
                }
                break;
        }
    }

    void removeRoad(Road r) {
        Point pos = r.getPosition();
        int x = pos.x / TILESIZE;
        int y = pos.y / TILESIZE;
        int len = r.getLength() / TILESIZE;
        switch (r.getDirection().getAngle()) {
            case 0:
                for (int k = x; k < x+len; k++) {
                    matrix.put(new Point(k, y), new Tile());
                }
                break;
            case 1:
                for (int k = y-len; k < y; k++) {
                    matrix.put(new Point(x, k), new Tile());
                }
                break;
            case 2:
                for (int k = x-len; k < x; k++) {
                    matrix.put(new Point(k, y), new Tile());
                }
                break;
            case 3:
                for (int k = y; k < y+len; k++) {
                    matrix.put(new Point(x, k), new Tile());
                }
                break;
        }
    }

    Tile[] getNeighboringTiles(int x, int y) {
        Tile[] neighbors = new Tile[4];
        // 0 = left
        // 1 = down
        // 2 = right
        // 3 = up
        neighbors[0] = this.get(x-1, y);
        neighbors[1] = this.get(x, y+1);
        neighbors[2] = this.get(x+1, y);
        neighbors[3] = this.get(x, y-1);
        return neighbors;
    }

    Tile[] getNeighboringTilesFromCoords(int x, int y) { return getNeighboringTiles(x/32, y/32); }

    void removeIntersection(Intersection i) {
        int x = i.getPosition().x / TILESIZE;
        int y = i.getPosition().y / TILESIZE;
        int w = i.getSize().width / TILESIZE;
        int h = i.getSize().height / TILESIZE;
        for (int j = 0; j < w; j++) {
            for (int k = 0; k < h; k++) {
                matrix.put(new Point(x+j, y+k), new Tile());
            }
        }
    }

    void addIntersection(Intersection i) {
        int x = i.getPosition().x / TILESIZE;
        int y = i.getPosition().y / TILESIZE;
        int w = i.getSize().width / TILESIZE;
        int h = i.getSize().height / TILESIZE;
        for (int j = 0; j < w; j++) {
            for (int k = 0; k < h; k++) {
                matrix.put(new Point(x+j, y+k), new Tile(i));
            }
        }
    }

    Tile get(int x, int y) {
        return matrix.getOrDefault(new Point(x, y), new Tile());
    }

    Tile getCoords(int x, int y) {
        return get(x/TILESIZE, y/TILESIZE);
    }

    void set(int x, int y, Tile t) {
        matrix.put(new Point(x, y), t);
    }

    void setCoords(int x, int y, Tile t) {
        set(x/TILESIZE, y/TILESIZE, t);
    }

    class Tile {
        TileType type = TileType.EMPTY;
        Road road = null;
        Intersection intersection = null;

        Tile() {}

        Tile(Road r) {
            type = (r.getDirection().isHorizontal() ? TileType.ROAD_H : TileType.ROAD_V);
            road = r;
        }

        Tile(Intersection i) {
            type = TileType.INTERSECTION;
            intersection = i;
        }

        boolean isEmpty() {
            return type == TileType.EMPTY;
        }

        public boolean isRoad() { return type == TileType.ROAD_H || type == TileType.ROAD_V; }

        boolean isIntersection() { return type == TileType.INTERSECTION; }
    }
}
