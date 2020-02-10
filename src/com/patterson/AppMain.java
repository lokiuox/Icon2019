package com.patterson;

import com.patterson.world.Surface;

import javax.swing.*;
import java.awt.*;


public class AppMain extends JFrame {

    public AppMain() {
        initUI();
        //KnowledgeBase.init("resources/KB.pl"); //Is this needed? We can init from the Scenario constructor
    }

    private void initUI() {
        Surface s = new Surface();
        add(s);

        pack();
        setTitle("Traffic2D: " + s.getSceneName());
        setSize(600,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AppMain app = new AppMain();
                app.setVisible(true);
            }
        });
    }
}