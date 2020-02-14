package com.patterson.utility;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KnowledgeBaseExchange extends KnowledgeBase {
    private Set<Packet> packetSet = new HashSet<>();
    //private Map<KnowledgeBaseExchange, Integer> lastMerge = new HashMap<>();
    //private int minExchangeTime = 30;


    int countTick = 0;

    public Set<Packet> getPacketSet() {
        return packetSet;
    }

    public void addPacket(Packet pkg) {
        packetSet.add(pkg);
    }

    public void addPackets(KnowledgeBaseExchange kb) {
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

        for (Packet p: packetSet) {
            p.tick();
            if (!p.isValid())
                packetSet.remove(p);
        }
    }
}
