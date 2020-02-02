package com.patterson;

import com.patterson.entity.Car;
import com.patterson.entity.Road;
import com.patterson.utility.Angle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Editor extends JFrame {

    public Editor() {

        initUI();
    }

    private void initUI() {

        add(new Surface());

        setTitle("Editor");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Editor ex = new Editor();
                ex.setVisible(true);
            }
        });
    }
}

class Surface extends JPanel {

    Pointer p = new Pointer(-16,-16);
    TempRoad t = new TempRoad(-16, -16, -16, -16);
    private Dictionary<String, Road> roads = new Hashtable<>();


    public Surface() {
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
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    public int toGrid(int x) {
        return 8+16*(Math.round(x/16));
    }

    class Pointer {
        private int x;
        private int y;

        public Pointer(int x, int y) {
            setPosition(x,y);
        }

        public void setPosition(int x, int y) {
            this.x = toGrid(x);
            this.y = toGrid(y);
        }

        public void draw(Graphics2D g) {

            Angle a = new Angle(0);

            for (int i=0; i<4; i++) {
                a.setAngle(i);
                g.drawLine(x, y, x+16*a.cos(), y+16*a.sin());
            }
        }
    }

    class TempRoad {
        boolean visible = false;
        int xStart;
        int yStart;
        int xEnd;
        int yEnd;

        public TempRoad(int xs, int ys, int xe , int ye) {
            set(xs, ys, xe, ye);
        }

        public void set(int xs, int ys, int xe , int ye) {
            xStart = toGrid(xs);
            yStart = toGrid(ys);

            if (Math.abs(xs-xe)>Math.abs(ys-ye)) {
                xEnd = toGrid(xe);
                yEnd = toGrid(ys);
            } else {
                xEnd = toGrid(xs);
                yEnd = toGrid(ye);
            }
        }

        public Angle direction() {
            Angle a = new Angle(0);

            if ( xEnd > xStart ) {
                a.setAngle(0);
            } else if ( yEnd < yStart ) {
                a.setAngle(1);
            } else if ( xEnd < xStart ) {
                a.setAngle(2);
            }else if ( yEnd > yStart ) {
                a.setAngle(3);
            }

            return a;
        }

        public int length() {
            int l = 0;

            if (direction().isHorizontal()) {
                l = Math.abs(xEnd-xStart);
            } else {
                l = Math.abs(yEnd-yStart);
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
                g.drawLine(xStart, yStart, xEnd, yEnd);
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
                roads.remove("R"+(roads.size()-1));
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
                r = new Road(t.xStart, t.yStart, t.direction().getAngle(), t.length());
                roads.put("R" + roads.size(), r);
            }

            repaint();
        }
    }
}
