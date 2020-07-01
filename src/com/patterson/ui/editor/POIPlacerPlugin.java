package com.patterson.ui.editor;

import com.patterson.entity.PointOfInterest;
import com.patterson.ui.MapView;
import com.patterson.utility.Angle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.patterson.ui.editor.MapEditorView.toGrid;

public class POIPlacerPlugin implements IEditorPlugin {
    private MapEditorView editor;
    private Pointer pointer = new Pointer(-16,-16);
    private MapEditorView.Highlighter highlighter;
    private MovingAdapter ma = new MovingAdapter();
    private PressAdapter pa = new PressAdapter();
    private PointOfInterest selectedPOI = null;

    POIPlacerPlugin(MapEditorView m) {
        this.editor = m;
        init();
    }

    @Override
    public void init() {
        highlighter = editor.highlighter;
    }

    private void repaint() {
        editor.repaint();
    }

    @Override
    public void draw(Graphics2D g2d) {
        pointer.draw(g2d);
    }

    @Override
    public void setMapView(MapView m) {
        setEditor((MapEditorView) m);
    }

    @Override
    public void setEditor(MapEditorView m) {
        this.editor = m;
    }

    @Override
    public void enable() {
        editor.addKeyListener(pa);
        editor.addMouseListener(ma);
        editor.addMouseMotionListener(ma);
    }

    @Override
    public void disable() {
        editor.removeKeyListener(pa);
        editor.removeMouseListener(ma);
        editor.removeMouseMotionListener(ma);
    }

    class Pointer {
        Point position = new Point();

        Pointer(int x, int y) {
            setPosition(x,y);
        }

        void setPosition(int x, int y) {
            this.position = toGrid(x, y);
        }

        public void draw(Graphics2D g) {

            Angle a = new Angle(0);

            for (int i=0; i<4; i++) {
                a.setAngle(i);
            }
            g.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
            g.setColor(Color.RED);
            g.drawRect(position.x-16, position.y-16, 32, 32);
        }
    }


    //Catch key presses
    class PressAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

        }
    }

    //Catch mouse actions
    class MovingAdapter extends MouseAdapter {
        private int x;
        private int y;

        @Override
        public void mouseMoved(MouseEvent e) {
            pointer.setPosition((int)(e.getX()/editor.getScaleFactor()), (int)(e.getY()/editor.getScaleFactor()));
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            x = (int)(e.getX()/editor.getScaleFactor());
            y = (int)(e.getY()/editor.getScaleFactor());

            MapMatrix.Tile t = editor.getMatrix().getCoords(x, y);

            if (SwingUtilities.isLeftMouseButton(e)) {
                if (t.isPOI()) {
                    highlighter.set(t.poi);
                    selectedPOI = t.poi;
                    highlighter.visible();
                } else if (t.isEmpty()) {
                    selectedPOI = null;
                    Point coords = toGrid(x, y);
                    editor.placePOI(coords.x, coords.y);
                    highlighter.invisible();
                } else {
                    highlighter.invisible();
                    selectedPOI = null;
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                if (selectedPOI != null) {
                    editor.removePOI(selectedPOI);
                    selectedPOI = null;
                    highlighter.invisible();
                }
            }
            repaint();
        }
    }
}