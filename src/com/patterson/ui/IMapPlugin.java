package com.patterson.ui;

import com.patterson.ui.MapView;

import java.awt.*;

public interface IMapPlugin {
    void init();
    void draw(Graphics2D g2d);
    void setMapView(MapView m);
    void enable();
    void disable();
}
