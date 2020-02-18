package com.patterson.ui.editor;

import com.patterson.entity.Car;
import com.patterson.entity.CarIE;

import com.patterson.utility.Angle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.patterson.ui.editor.MapEditorView.toGrid;

public class CarPositioningMode implements IEditorMode {
    private enum CarType {CAR, /*CAR_GREEN, CAR_RED,*/ CAR_IE}
    private static CarType[] car_types = CarType.values();
    private MapEditorView editor;
    private Pointer pointer = new Pointer(-16,-16);
    private MovingAdapter ma = new MovingAdapter();
    private PressAdapter pa = new PressAdapter();
    private CarType selected_car_type = CarType.CAR;
    private Car selectedCar = null;

    CarPositioningMode(MapEditorView m) {
        this.editor = m;
        init();
    }

    @Override
    public void init() {}

    private void repaint() {
        editor.repaint();
    }

    @Override
    public void draw(Graphics2D g2d) {
        pointer.draw(g2d);
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
    }

    @Override
    public void deactivate() {
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
                    int index = (selected_car_type.ordinal() == 0 ? car_types.length-1 : selected_car_type.ordinal() - 1);
                    selected_car_type = car_types[index];
                    System.out.println(selected_car_type + " selected.");
                    break;
                case KeyEvent.VK_RIGHT:
                    selected_car_type = car_types[(selected_car_type.ordinal() + 1)%car_types.length];
                    System.out.println(selected_car_type + " selected.");
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
            pointer.setPosition(e.getX(),e.getY());
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();

            MapMatrix.Tile t = editor.getMatrix().getCoords(x, y);
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (t.isRoad()) {
                    if (t.hasCar()) {
                        System.err.println("There's already a car here.");
                    } else {
                        Car c;
                        Point pos = toGrid(x, y);
                        switch (selected_car_type) {
                            case CAR:
                                c = new Car(Car.nextID(), pos.x, pos.y, t.road.getDirection().getAngle());
                                break;
                            /*case CAR_GREEN:
                                c = new Car_green(Car.nextID(), pos.x, pos.y, t.road.getDirection().getAngle());
                                break;
                            case CAR_RED:
                                c = new Car_red(Car.nextID(), pos.x, pos.y, t.road.getDirection().getAngle());
                                break;*/
                            case CAR_IE:
                                c = new CarIE(Car.nextID(), pos.x, pos.y, t.road.getDirection().getAngle());
                                break;
                            default:
                                System.err.println("Car type not recognized.");
                                return;
                        }
                        c.addRoad(t.road);
                        editor.addCar(c);
                    }
                } else {
                    System.err.println("Cannot put a car there.");
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                if (selectedCar != null) {
                    editor.removeCar(selectedCar);
                    selectedCar = null;
                }
            }
            repaint();
        }
    }
}
