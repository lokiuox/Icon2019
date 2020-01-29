package com.patterson.entity;

import javax.swing.*;

public class Car_red extends Car {

    public Car_red(float x, float y, int d) {
        super(x, y, d);
    }

    @Override
    protected void loadImage() {
        img[0] = new ImageIcon("resources/car/car0_red.png").getImage();
        img[1] = new ImageIcon("resources/car/car1_red.png").getImage();
        img[2] = new ImageIcon("resources/car/car2_red.png").getImage();
        img[3] = new ImageIcon("resources/car/car3_red.png").getImage();
    }
}
