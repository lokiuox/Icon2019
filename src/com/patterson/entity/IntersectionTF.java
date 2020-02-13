package com.patterson.entity;

public class IntersectionTF extends Intersection {

    int countTick = 0;
    int switchTime = 30*5;

    public IntersectionTF(String id, float x, float y, int w, int h) {
        super(id, x, y, w, h);
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
