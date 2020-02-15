package com.patterson.entity;

import java.util.List;

public interface INavigator {
    List<String> calculatePath(String startRoad, String endRoad);
}
