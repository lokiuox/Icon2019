package com.patterson.entity;

import java.util.LinkedList;

public interface INavigator {
    LinkedList<String> calculatePath(String startRoad, String endRoad);
}
