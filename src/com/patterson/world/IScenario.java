package com.patterson.world;

import java.awt.Graphics2D;

public interface IScenario {
    void draw(Graphics2D g);
    void tick();
}
