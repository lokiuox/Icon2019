package com.patterson.utility;

public class Packet extends KnowledgeBase {
    int ttl = 30*30;    //time to live. default 30s

    public Packet() {
        super();
    }

    public Packet(int t) {
        ttl = t;
    }

    public void tick() {
        ttl--;
    }

    public boolean isValid() {
        return ttl > 0;
    }
}
