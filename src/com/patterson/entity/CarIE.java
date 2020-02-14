package com.patterson.entity;

import com.patterson.utility.KnowledgeBaseIF;
import org.json.JSONObject;

public class CarIE extends Car {

    KnowledgeBaseIF kb = new KnowledgeBaseIF();

    public CarIE(String id, float x, float y, int d) {
        super(id, x, y, d);
    }

    public CarIE(JSONObject jo_car) {super(jo_car);}

    @Override
    protected void roadEnd() {

        // recalculate path before each intersection
        if (path.peek() != null && navigator != null && !isNearCar() && greenTF() && rightToPass && !passing) {
            destination = randomDestination();
            calculatePath();
            rightToPass = false;
        }

        super.roadEnd();
    }

    @Override
    protected void calculatePath() {
        kb.assertToKB();
        super.calculatePath();
        kb.retractFromKB();
    }
}
