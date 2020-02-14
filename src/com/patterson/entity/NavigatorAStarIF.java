package com.patterson.entity;

import java.util.LinkedList;

public class NavigatorAStarIF extends NavigatorAStar {

    private void updateWeights() {

    }

    @Override
    public LinkedList<String> calculatePath(String startRoad, String endRoad) {
        updateWeights();
        return super.calculatePath(startRoad, endRoad);
    }
}
