package com.patterson.world;

import com.patterson.entity.Road;
import com.patterson.utility.Angle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

class Editor extends MapView {

    Pointer p = new Pointer(-16,-16);
    TempRoad t = new TempRoad(-16, -16, -16, -16);
    private Dictionary<String, Road> roads = new Hashtable<>();


    public Editor() {
        initUI();
    }

    private void initUI() {
        setFocusable(true);
        addKeyListener(new PressAdapter());
        MovingAdapter ma = new MovingAdapter();
        addMouseMotionListener(ma);
        addMouseListener(ma);
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        p.draw(g2d);
        t.draw(g2d);

        Enumeration<Road> e = roads.elements();
        while (e.hasMoreElements()) {
            e.nextElement().draw(g2d);
        }
        // Make the mouse invisible inside editor area
        this.setCursor(this.getToolkit().createCustomCursor(
                new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),
                new Point(),
                null ));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    protected Point toGrid(int x, int y) {
        return new Point(
                32*(x/32),
                32*(y/32)
        );
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
                g.drawLine(position.x, position.y, position.x+16*a.cos(), position.y+16*a.sin());
            }
        }
    }

    class TempRoad {
        boolean visible = false;
        Point start;
        Point end;

        public TempRoad(int xs, int ys, int xe , int ye) {
            set(xs, ys, xe, ye);
        }

        public void set(int xs, int ys, int xe , int ye) {
            start = toGrid(xs, ys);

            if (Math.abs(xs-xe)>Math.abs(ys-ye)) {
                end = toGrid(xe, ys);
            } else {
                end = toGrid(xs, ye);
            }
            if (direction().isHorizontal()) {
                start.x -= 16;
                end.x += 16;
            } else {
                start.y -= 16;
                end.y += 16;
            }
        }

        public Angle direction() {
            Angle a = new Angle(0);

            if ( end.x > start.x ) {
                a.setAngle(0);
            } else if ( end.y < start.y ) {
                a.setAngle(1);
            } else if ( end.x < start.x ) {
                a.setAngle(2);
            }else if ( end.y > start.y ) {
                a.setAngle(3);
            }

            return a;
        }

        public int length() {
            int l = 0;

            if (direction().isHorizontal()) {
                l = Math.abs(end.x-start.x);
            } else {
                l = Math.abs(end.y-start.y);
            }
            return l;
        }

        public void visible() {
            visible = true;
        }

        public void invisible() {
            visible = false;
        }

        public void draw(Graphics2D g) {
            if (visible) {
                g.drawLine(start.x, start.y, end.x, end.y);
            }
        }
    }

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
                t.set(x,y,x,y);
                t.visible();
            } else if (SwingUtilities.isRightMouseButton(e) && roads.size()>0) {
                roads.remove("r"+(roads.size()-1));
            }

            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            t.set(x,y,e.getX(),e.getY());

            p.setPosition(e.getX(),e.getY());
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Road r;

            t.invisible();

            if (t.length()>0 && SwingUtilities.isLeftMouseButton(e)) {
                r = new Road("r" + roads.size(), t.start.x, t.start.y, t.direction().getAngle(), t.length());
                roads.put("r" + roads.size(), r);
            }

            repaint();
        }
    }
}