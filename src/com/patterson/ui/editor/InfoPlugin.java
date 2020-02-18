package com.patterson.ui.editor;

import com.patterson.entity.Intersection;
import com.patterson.entity.Road;
import com.patterson.ui.MapView;
import com.patterson.utility.Angle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.patterson.ui.editor.MapEditorView.toGrid;

public class InfoPlugin implements IEditorPlugin {
    private MapEditorView editor;
    private Pointer pointer = new Pointer(-16,-16);
    private MapEditorView.Highlighter highlighter;
    private MovingAdapter ma = new MovingAdapter();
    private PressAdapter pa = new PressAdapter();
    private Road selectedRoad = null;
    private Intersection selectedIntersection = null;

    InfoPlugin(MapEditorView m) {
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
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    System.out.println("left");
                    break;
                case KeyEvent.VK_RIGHT:
                    System.out.println("right");
                    break;
            }
            repaint();
        }
    }

    //Catch mouse actions
    class MovingAdapter extends MouseAdapter {
        private int x;
        private int y;

        @Override
        public void mouseMoved(MouseEvent e) {
            pointer.setPosition(e.getX(), e.getY());
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();

            if (SwingUtilities.isLeftMouseButton(e)) {
                selectedRoad = null;
                selectedIntersection = null;
                MapMatrix.Tile t = editor.getMatrix().getCoords(x, y);
                Point pos = toGrid(x, y);
                pos.setLocation(pos.x/32, pos.y/32);
                System.out.println("=================================================================");
                System.out.println("Selected Tile: " + pos.x + "," + pos.y +": ");
                if (t.isEmpty()) {
                    System.out.println("Empty");
                } else if (t.isRoad()) {
                    Road r = t.road;
                    selectedRoad = r;
                    highlighter.set(r);
                    System.out.println("Road " + r.getID());
                    System.out.println("Type: " + r.getType());
                    System.out.println("Pos in pixels: " + r.getPosition().x + "," + r.getPosition().y + "px = " + r.getPosition().x/32 + "," + r.getPosition().y/32 + " tiles");
                    System.out.println("Length: " + r.getLength() + "px = " + r.getLength()/32 + " tiles");
                    System.out.println((r.getDirection().isHorizontal() ? "Horizontal" : "Vertical"));
                    String direction;
                    switch (r.getDirection().getAngle()) {
                        case 0:
                            direction = "Left to Right";
                            break;
                        case 1:
                            direction = "Down to Up";
                            break;
                        case 2:
                            direction = "Right to Left";
                            break;
                        case 3:
                            direction = "Up to Down";
                            break;
                        default:
                            direction = "unknown";
                            break;
                    }
                    System.out.println("Direction: " + r.getDirection().getAngle() + ": " + direction);
                    System.out.println("Insersection: " + (r.getIntersection() == null ? "null" : r.getIntersection().getID()));
                    System.out.println("End: " + r.getEnd().x/32 + "," + r.getEnd().y/32);
                    int next_x = r.getEnd().x/32 + Math.min(0, r.getDirection().cos());
                    int next_y = r.getEnd().y/32 + Math.min(0, r.getDirection().sin());
                    System.out.println("Next: " + next_x + "," + next_y);
                } else if (t.isIntersection()) {
                    Intersection i = t.intersection;
                    selectedIntersection = i;
                    highlighter.set(i);
                    System.out.println("Intersection " + i.getID());
                    System.out.println("Type: " + i.getType());
                    System.out.println("Pos in pixels: " + i.getPosition().x + "," + i.getPosition().y);
                    System.out.println("Dimensions: " + i.getSize().width/32 + "x" + i.getSize().height/32 + " tiles");
                    System.out.println("Incoming Roads:");
                    for (Road r: i.getRoads())
                        System.out.println("- " + r.getID());
                    System.out.println("Outgoing Roads:");
                    for (Road r: editor.getIntersectionOutgoingRoads(i))
                        System.out.println("- " + r.getID());
                }
                System.out.println("=================================================================");
            }
            repaint();
        }
    }
}
