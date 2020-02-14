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
    long countTick;

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

    @Override
    public void tick() {
        super.tick();
        kb.tick();
        countTick++;
    }

    public void sendInformation(CarIE c) {

        // send information to each car every minSendTime
        if (!lastSent.containsKey(c) || (lastSent.containsKey(c) && countTick-lastSent.get(c)>minSendTime)) {
            lastSent.put(c,countTick);

            Packet p = new Packet();
            p.addAssertion("strada_corrente("+ getID() + "," + road.getID() +")");
            p.addAssertion("velocita("+ getID() + "," + speed +")");

            c.getKB().addPacket(p);     // invia posizione e velocità correnti
            c.getKB().addPackets(kb);   // invia tutti i paccketti nella KB
        }
    }

    public KnowledgeBaseIF getKB() {
        return kb;
    }
}
