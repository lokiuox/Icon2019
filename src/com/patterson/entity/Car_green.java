package com.patterson.entity;

import org.json.JSONObject;

import javax.swing.ImageIcon;

public class Car_green extends Car {


    public Car_green(String id, float x, float y, int d) {
        super(id, x, y, d);
    }

    public Car_green(JSONObject jo_car) {super(jo_car);}

    @Override
    public String getType() { return "CarGreen"; }

    @Override
    protected void loadImage() {
        img[0] = new ImageIcon("resources/car/car0_green.png").getImage();
        img[1] = new ImageIcon("resources/car/car1_green.png").getImage();
        img[2] = new ImageIcon("resources/car/car2_green.png").getImage();
        img[3] = new ImageIcon("resources/car/car3_green.png").getImage();
    }
}
