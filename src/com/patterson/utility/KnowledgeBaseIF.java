package com.patterson.utility;

import java.util.HashSet;
import java.util.Set;

public class KnowledgeBaseIF extends KnowledgeBase {
    private Set<Packet> packetSet = new HashSet<>();
    //private Map<KnowledgeBaseExchange, Integer> lastMerge = new HashMap<>();
    //private int minExchangeTime = 30;

    long countTick = 0;

    public Set<Packet> getPacketSet() {
        return packetSet;
    }

    public void addPacket(Packet pkg) {
        packetSet.add(pkg);
    }

    public void addPackets(KnowledgeBaseIF kb) {
        packetSet.addAll(kb.getPacketSet());
    }

    @Override
    public void assertToKB() {
        super.assertToKB();
        for (Packet p: packetSet)
            p.assertToKB();
    }

    @Override
    public void retractFromKB() {
        super.retractFromKB();
        for (Packet p: packetSet)
            p.retractFromKB();
    }

    public void tick() {
        countTick++;

        HashSet<Packet> tSet = new HashSet<>(packetSet);

        for (Packet p: tSet) {
            p.tick();
            if (!p.isValid())
                packetSet.remove(p);
        }
    }

    @Override
    public void print() {
        super.print();
        for (Packet p: packetSet) {
            p.print();
        }
    }
}