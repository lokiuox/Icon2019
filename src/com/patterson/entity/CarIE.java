package com.patterson.entity;

import com.patterson.utility.KnowledgeBaseIF;
import com.patterson.utility.Packet;
import org.json.JSONObject;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CarIE extends Car {

    KnowledgeBaseIF kb = new KnowledgeBaseIF();
    private Map<CarIE, Long> lastSent = new HashMap<>();
    private int minSendTime = 30*5;
    private long countTick;
    private long logTime = 30;

    public CarIE(String id, float x, float y, int d) {
        super(id, x, y, d);
        navigator = new NavigatorProlog();
    }

    public CarIE(JSONObject jo_car) {
        super(jo_car);
        navigator = new NavigatorAStarIF();
    }

    @Override
    public String getType() { return "CarIE"; }

    @Override
    protected void loadImage() {
        img[0] = new ImageIcon("resources/car/car0_red.png").getImage();
        img[1] = new ImageIcon("resources/car/car1_red.png").getImage();
        img[2] = new ImageIcon("resources/car/car2_red.png").getImage();
        img[3] = new ImageIcon("resources/car/car3_red.png").getImage();
    }

    @Override
    protected void calculatePath() {
        kb.assertToKB();
        //super.calculatePath();
        setPath(navigator.calculatePath(road.getID(), destination));
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

            c.getKB().addPackets(kb.getPacketSet());   // invia tutti i paccketti nella KB

            // stampa contenuto kb
            //System.out.println(getID() + " KB: ");
            //kb.print();
            //System.out.println();
        }
    }

    public KnowledgeBaseIF getKB() {
        return kb;
    }
}