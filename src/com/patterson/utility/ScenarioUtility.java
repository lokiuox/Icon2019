package com.patterson.utility;

import com.patterson.entity.Road;
import com.patterson.world.Scenario;

public class ScenarioUtility {
    private static Scenario scenario;

    public static void setScenario(Scenario s) {
        scenario = s;
    }

    public static Road getRoadByID(String s) {
        return scenario.getRoadByID(s);
    }
}