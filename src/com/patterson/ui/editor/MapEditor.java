package com.patterson.ui.editor;

import com.patterson.entity.Road;
import com.patterson.ui.MapView;
import com.patterson.utility.Angle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

class MapEditor extends MapView {

    private Pointer p = new Pointer(-16,-16);
    private TempRoad t = new TempRoad(-16, -16, -16, -16);
    private Map<String, Road> roads = new HashMap<>();



    public MapEditor() {
        initUI();
    }

    private void initUI() {
        setFocusable(true);
        addKeyListener(new PressAdapter());
        MovingAdapter ma = new MovingAdapter();
        addMouseMotionListener(ma);
        addMouseListener(ma);
        // Make the mouse invisible inside editor area
        this.setCursor(this.getToolkit().createCustomCursor(
                new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),
                new Point(),
                null ));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        p.draw(g2d);
        t.draw(g2d);
        for (Road r: roads.values())
            r.draw(g2d);
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
                //g.drawLine(position.x, position.y, position.x+16*a.cos(), position.y+16*a.sin());
            }
            g.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
            g.drawLine(position.x-16, position.y-16, position.x-16, position.y+16);
            g.drawLine(position.x-16, position.y+16, position.x+16, position.y+16);
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
                end.x -= 16;
            } else {
                start.y += 16;
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
                //g.drawLine(start.x, start.y, end.x, end.y);
                /*
                g.setColor(Color.RED);
                if (direction().isHorizontal()) {
                    g.drawLine(start.x, start.y-16, end.x, end.y-16);
                    g.drawLine(start.x, start.y+16, end.x, end.y+16);
                } else {
                    g.drawLine(start.x-16, start.y, end.x-16, end.y);
                    g.drawLine(start.x+16, start.y, end.x+16, end.y);
                }
                */
                Image[] img = new Image[4];
                img[0] = new ImageIcon("resources/road/roadH_top.png").getImage();
                img[1] = new ImageIcon("resources/road/roadH_bottom.png").getImage();
                img[2] = new ImageIcon("resources/road/roadV_left.png").getImage();
                img[3] = new ImageIcon("resources/road/roadV_right.png").getImage();
                for (int i=8; i<=length()-8; i+=16) {
                    if (direction().isHorizontal()) {
                        g.drawImage(img[0], start.x + i * direction().cos() - 8,  start.y + i * direction().sin(), null);
                        g.drawImage(img[1], start.x + i * direction().cos() - 8,  start.y + i * direction().sin() - 16, null);

                    } else {
                        g.drawImage(img[2], start.x + i * direction().cos(),       start.y + i * direction().sin() - 8, null);
                        g.drawImage(img[3], start.x + i * direction().cos() - 16,  start.y + i * direction().sin() - 8, null);
                    }
                }
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
                r = new Road("r" + roads.size(), t.start.x, t.start.y, t.direction().getAngle(), t.length());
                roads.put("r" + roads.size(), r);
            }

            repaint();
        }
    }
}
