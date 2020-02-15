package com.patterson.entity;

import org.json.JSONObject;

public class IntersectionTF extends Intersection {

    private int countTick = 0;
    private int switchTime = 30*5;

    public IntersectionTF(String id, int x, int y, int w, int h) {
        super(id, x, y, w, h);
    }

    public IntersectionTF(JSONObject jo_intersection) {
        super(jo_intersection);
    }

    private void initTF() {
        for (Road r : roads) {
            if (r instanceof RoadTF) {
                if (r.direction.isVertical())
                    ((RoadTF) r).setGreen();
                else
                    ((RoadTF) r).setRed();
            }
        }
    }

    public void tick () {

        if (countTick == 0)
            initTF();

        if (countTick > switchTime) {
            countTick = 0;

            for (Road r : roads)
                if (r instanceof RoadTF)
                    ((RoadTF) r).switchTF();
        }
        countTick++;
    }
}