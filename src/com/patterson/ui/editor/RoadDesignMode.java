package com.patterson.ui.editor;

import com.patterson.entity.Road;
import com.patterson.entity.RoadStop;
import com.patterson.entity.RoadTF;
import com.patterson.utility.Angle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static com.patterson.ui.editor.MapEditorView.toGrid;

public class RoadDesignMode implements IEditorMode {
    private enum RoadType { ROAD, ROAD_STOP, ROAD_TF }
    private static RoadType[] road_types = RoadType.values();
    private RoadType selected_road_type = RoadType.ROAD;
    private MapEditorView editor;
    private Pointer p = new Pointer(-16,-16);
    private TempRoad tempRoad = new TempRoad(-16, -16, -16, -16);
    private MapEditorView.Highlighter highlighter;
    private MovingAdapter ma = new MovingAdapter();
    private PressAdapter pa = new PressAdapter();
    private Road selectedRoad = null;

    RoadDesignMode(MapEditorView m) {
        this.editor = m;
        init();
    }

    @Override
    public void init() {
        this.highlighter = editor.highlighter;
    }

    private void repaint() {
        editor.repaint();
    }

    @Override
    public void draw(Graphics2D g2d) {
        tempRoad.draw(g2d);
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
        editor.setCursor(editor.getToolkit().createCustomCursor(
                new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),
                new Point(),
                null ));
    }

    @Override
    public void deactivate() {
        editor.removeKeyListener(pa);
        editor.removeMouseListener(ma);
        editor.removeMouseMotionListener(ma);
        editor.setCursor(Cursor.getDefaultCursor());
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
            g.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
            g.setColor(Color.RED);
            g.drawLine(position.x-16, position.y-16, position.x-16, position.y+16);
            g.drawLine(position.x-16, position.y+16, position.x+16, position.y+16);
        }
    }

    class TempRoad {
        boolean visible = false;
        Point start;
        Point end;

        TempRoad(int xs, int ys, int xe, int ye) {
            set(xs, ys, xe, ye);
        }

        void set(int xs, int ys, int xe, int ye) {
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

        Angle direction() {
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

        int length() {
            int l;

            if (direction().isHorizontal()) {
                l = Math.abs(end.x-start.x);
            } else {
                l = Math.abs(end.y-start.y);
            }
            return l;
        }

        void visible() {
            visible = true;
        }

        void invisible() {
            visible = false;
        }

        public void draw(Graphics2D g) {
            if (visible) {
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
                    int index = (selected_road_type.ordinal() == 0 ? road_types.length-1 : selected_road_type.ordinal() - 1);
                    selected_road_type = road_types[index];
                    System.out.println(selected_road_type + " selected.");
                    break;
                case KeyEvent.VK_RIGHT:
                    selected_road_type = road_types[(selected_road_type.ordinal() + 1)%road_types.length];
                    System.out.println(selected_road_type + " selected.");
                    break;
                case KeyEvent.VK_C:
                    if (selectedRoad != null) {
                        editor.changeRoadType(selectedRoad);
                        selectedRoad = null;
                        highlighter.invisible();
                    } else {
                        System.err.println("No roads selected");
                    }
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
            MapMatrix.Tile t = editor.getMatrix().getCoords(x, y);
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (t.isRoad()) {
                    selectedRoad = t.road;
                    highlighter.set(t.road);
                } else {
                    selectedRoad = null;
                    highlighter.invisible();
                }
                tempRoad.set(x, y, x, y);
                tempRoad.visible();
            } else if (SwingUtilities.isRightMouseButton(e) && editor.getRoadsSize()>0) {
                if (selectedRoad != null) {
                    editor.removeRoad(selectedRoad);
                    selectedRoad = null;
                    highlighter.invisible();
                }
            }
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            tempRoad.set(x,y,e.getX(),e.getY());
            if (tempRoad.direction().isHorizontal())
                p.setPosition(e.getX(), y);
            else
                p.setPosition(x, e.getY());
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (tempRoad.length()>0 && SwingUtilities.isLeftMouseButton(e)) {
                tempRoad.invisible();
                Road r;
                switch (selected_road_type) {
                    case ROAD:
                        r = new Road(Road.nextID(), tempRoad.start.x, tempRoad.start.y, tempRoad.direction().getAngle(), tempRoad.length());
                        break;
                    case ROAD_STOP:
                        r = new RoadStop(Road.nextID(), tempRoad.start.x, tempRoad.start.y, tempRoad.direction().getAngle(), tempRoad.length());
                        break;
                    case ROAD_TF:
                        r = new RoadTF(Road.nextID(), tempRoad.start.x, tempRoad.start.y, tempRoad.direction().getAngle(), tempRoad.length());
                        break;
                    default:
                        System.err.println("Road Type not recognized.");
                        return;
                }
                editor.placeRoad(r);
                selectedRoad = r;
            }
            repaint();
        }
    }
}
