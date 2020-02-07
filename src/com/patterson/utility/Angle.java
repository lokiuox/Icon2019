package com.patterson.utility;

import java.awt.geom.Point2D;
import java.util.Objects;

public class Angle {
    private int angle;

    public Angle(int angle) {
        setAngle(angle);
    }

    public Angle(Angle angle) {
        setAngle(angle);
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = Math.floorMod(angle,4);
    }

    public void setAngle(Angle angle) {
        this.angle = angle.getAngle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Angle angle1 = (Angle) o;
        return angle == angle1.angle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(angle);
    }

    public int cos() {
        int i=0;
        switch (angle) {
            case 0:
                i = 1;
                break;
            case 1:
                i = 0;
                break;
            case 2:
                i = -1;
                break;
            case 3:
                i = 0;
                break;
        }
        return i;
    }

    public int sin() {
        int i=0;
        switch (angle) {
            case 0:
                i = 0;
                break;
            case 1:
                i = -1;
                break;
            case 2:
                i = 0;
                break;
            case 3:
                i = 1;
                break;
        }
        return i;
    }

    public void rotate(int add) {
        angle += add;
        angle = Math.floorMod(angle,4);
    }

    // Dato un punto (s) e un vettore, identificato da un punto (d) e un angolo (this)
    // restituisce l'angolo della normale al vettore
    public Angle ortho(Point2D s, Point2D d) {

        Angle a = new Angle(0);

        if (isVertical()) {
            if (s.getX()<d.getX()) {
                a.setAngle(0);
            } else {
                a.setAngle(2);
            }
        } else {
            if (s.getY()>d.getY()) {
                a.setAngle(1);
            } else {
                a.setAngle(3);
            }
        }
        return a;
    }

    public boolean isHorizontal() {
        return angle%2 == 0;
    }

    public boolean isVertical() {
        return angle%2 != 0;
    }
}
