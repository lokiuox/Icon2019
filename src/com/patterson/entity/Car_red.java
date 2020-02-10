package com.patterson.entity;

import javax.swing.ImageIcon;

public class Car_red extends Car {

    public Car_red(String id, float x, float y, int d) {
        super(id, x, y, d);
    }

    @Override
    protected void loadImage() {
        img[0] = new ImageIcon("resources/car/car0_red.png").getImage();
        img[1] = new ImageIcon("resources/car/car1_red.png").getImage();
        img[2] = new ImageIcon("resources/car/car2_red.png").getImage();
        img[3] = new ImageIcon("resources/car/car3_red.png").getImage();
    }
}
