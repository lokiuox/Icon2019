package com.patterson.entity;

import com.patterson.algorithms.Graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NavigatorAStarIF extends NavigatorAStar {

    private void updateWeights() {
        for(Map.Entry<String, Graph.Vertex<String>> entry : v.entrySet()){
            entry.getValue().setWeight(nu.getWeight(entry.getKey()));
        }

    }

    @Override
    public List<String> calculatePath(String startRoad, String endRoad) {
        updateWeights();
        return super.calculatePath(startRoad, endRoad);
    }
}
