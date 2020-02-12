package com.patterson.ui.editor;

import com.patterson.ui.MapView;

import java.awt.*;

public interface IEditorMode {
    void init();
    void draw(Graphics2D g2d);
    void setEditor(MapEditorView m);
    void activate();
    void deactivate();
}
