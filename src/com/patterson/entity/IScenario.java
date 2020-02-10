package com.patterson.entity;

import java.awt.Graphics2D;

public interface IScenario {
    public void draw(Graphics2D g);
    public void tick();
}
