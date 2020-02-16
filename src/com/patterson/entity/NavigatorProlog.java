package com.patterson.entity;

import com.patterson.utility.KnowledgeBase;

import java.util.LinkedList;
import java.util.List;

public class NavigatorProlog implements INavigator {
    @Override
    public List<String> calculatePath(String startRoad, String endRoad) {

        String startIntersection;
        String endIntersection;
        List<String> path = new LinkedList<>();

        startIntersection = KnowledgeBase.stringQuery("collega(S,"+ startRoad +",E).").iterator().next().get("E");
        endIntersection = KnowledgeBase.stringQuery("collega(S,"+ endRoad +",E).").iterator().next().get("S");

        List<String> intersectionPath = KnowledgeBase.listQuery("piubreve("+startIntersection+","+endIntersection+",P,L).").get("P");

        for(int i=1; i<intersectionPath.size(); i++) {
            String road = KnowledgeBase.stringQuery("collega("+intersectionPath.get(i-1)+",R,"+intersectionPath.get(i)+").").iterator().next().get("R");
            path.add(road);
        }

        path.add(endIntersection);

        return path;
    }
}