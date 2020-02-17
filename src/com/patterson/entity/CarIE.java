package com.patterson.entity;

import com.patterson.utility.KnowledgeBaseIF;
import com.patterson.utility.Packet;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CarIE extends Car {

    KnowledgeBaseIF kb = new KnowledgeBaseIF();
    private Map<CarIE, Long> lastSent = new HashMap<>();
    private int minSendTime = 30*5;
    private long countTick;
    private long logTime = 30;

    public CarIE(String id, float x, float y, int d) {
        super(id, x, y, d);
        navigator = new NavigatorAStarIF();
    }

    public CarIE(JSONObject jo_car) {super(jo_car);}

    @Override
    public String getType() { return "CarIE"; }
/*
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
*/
    @Override
    protected void calculatePath() {
        kb.assertToKB();
        super.calculatePath();
        kb.retractFromKB();
    }

    @Override
    public void tick() {
        super.tick();
        kb.tick();

        if (countTick%logTime == 0) {
            updateLog();
        }

        countTick++;
    }

    private void updateLog() {
        Packet p = new Packet();
        p.addAssertion("velocita("+ getID() + "," + speed +"," + road.getID() + ")");

        kb.addPacket(p);     // aggiunge velocità e posizioni correnti
    }

    public void sendInformation(CarIE c) {

        // send information to each car every minSendTime
        if (!lastSent.containsKey(c) || (lastSent.containsKey(c) && countTick-lastSent.get(c)>minSendTime)) {
            lastSent.put(c,countTick);

            c.getKB().addPackets(kb);   // invia tutti i paccketti nella KB
        }

        System.out.println(getID() + "KB: ");
        kb.print();
        System.out.println("");
    }

    public KnowledgeBaseIF getKB() {
        return kb;
    }
}