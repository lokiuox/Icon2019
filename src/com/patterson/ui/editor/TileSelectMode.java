package com.patterson.ui.editor;

import com.patterson.entity.Intersection;
import com.patterson.entity.Road;
import com.patterson.utility.Angle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Map;

import static com.patterson.ui.editor.MapEditorView.toGrid;

public class TileSelectMode implements IEditorMode {
    private MapEditorView editor;
    private Pointer p = new Pointer(-16,-16);
    private HighlightRect h = new HighlightRect();
    private MovingAdapter ma = new MovingAdapter();
    private PressAdapter pa = new PressAdapter();

    public TileSelectMode(MapEditorView m) {
        this.editor = m;
        init();
    }

    @Override
    public void init() {

    }

    public void repaint() {
        editor.repaint();
    }

    @Override
    public void draw(Graphics2D g2d) {
        h.draw(g2d);
        p.draw(g2d);
    }

    @Override
    public void setEditor(MapEditorView m) {
        this.editor = m;
    }

    @Override
    public void activate() {
        editor.addKeyListener(pa);
        editor.addMouseListener(ma);
        editor.addMouseMotionListener(ma);
        /*
        editor.setCursor(editor.getToolkit().createCustomCursor(
                new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),
                new Point(),
                null ));
         */
    }

    @Override
    public void deactivate() {
        editor.removeKeyListener(pa);
        editor.removeMouseListener(ma);
        editor.removeMouseMotionListener(ma);
        //editor.setCursor(Cursor.getDefaultCursor());
    }

    class Pointer {
        Point position = new Point();

        public Pointer(int x, int y) {
            setPosition(x,y);
        }

        public void setPosition(int x, int y) {
            this.position = toGrid(x, y);
        }

        public void draw(Graphics2D g) {

            Angle a = new Angle(0);

            for (int i=0; i<4; i++) {
                a.setAngle(i);
                //g.drawLine(position.x, position.y, position.x+16*a.cos(), position.y+16*a.sin());
            }
            g.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
            g.setColor(Color.RED);
            //g.drawLine(position.x-16, position.y-16, position.x-16, position.y+16);
            //g.drawLine(position.x-16, position.y+16, position.x+16, position.y+16);
            g.drawRect(position.x-16, position.y-16, 32, 32);
        }
    }

    class HighlightRect {
        boolean visible = false;
        Point start = new Point();
        Point dimensions = new Point();

        public HighlightRect() {

        }

        public HighlightRect(int x, int y) {
            set(x, y);
        }

        public HighlightRect(int x, int y, int w, int h) {
            set(x, y, w, h);
        }

        public HighlightRect(Road r) {
            set(r);
        }

        public void set(Road r) {
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

        public void set(int x, int y) {
            start = toGrid(x, y);
            start.x -= 16;
            start.y -= 16;
            dimensions.setLocation(32, 32);
        }

        public void set(int xs, int ys, int w , int h) {
            start = toGrid(xs, ys);
            start.x -= 16;
            start.y -= 16;
            dimensions.setLocation(w, h);
        }

        public void visible() {
            visible = true;
        }

        public void invisible() {
            visible = false;
        }

        public void draw(Graphics2D g) {
            if (visible) {
                //g.drawLine(start.x, start.y, end.x, end.y);
                g.setColor(Color.RED);
                //g.setStroke(new BasicStroke(2));
                g.drawRect(start.x, start.y, dimensions.x, dimensions.y);
                g.setColor(new Color(255, 0, 0 , 127));
                g.fillRect(start.x, start.y, dimensions.x, dimensions.y);
                g.setColor(Color.black);
            }
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
            p.setPosition(e.getX(),e.getY());
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();

            if (SwingUtilities.isLeftMouseButton(e)) {
                MapMatrix.Tile t = editor.getMatrix().getCoords(x, y);
                if (t.type == MapMatrix.TileType.ROAD_V || t.type == MapMatrix.TileType.ROAD_H)
                    h.set(t.road);
                else
                    h.set(x,y);
                h.visible();
            } else if (SwingUtilities.isRightMouseButton(e)) {
                h.invisible();
            }
            repaint();
        }

        /*
        @Override
        public void mouseDragged(MouseEvent e) {

            t.set(x,y,e.getX(),e.getY());
            if (t.direction().isHorizontal())
                p.setPosition(e.getX(),y);
            else
                p.setPosition(x, e.getY());
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Road r;
            t.invisible();

            if (t.length()>0 && SwingUtilities.isLeftMouseButton(e)) {
                r = new Road("r" + editor.getRoadsSize(), t.start.x, t.start.y, t.direction().getAngle(), t.length());
                editor.addRoad(r);
            }
            repaint();
        }
        */
    }
}
