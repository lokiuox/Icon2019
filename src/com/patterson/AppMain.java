package com.patterson;

import com.patterson.world.EditorWindow;
import com.patterson.world.MapWindow;

import javax.swing.*;
import java.awt.*;


public class AppMain extends JFrame {

    public AppMain() {
        initUI();
        //KnowledgeBase.init("resources/KB.pl"); //Is this needed? We can init from the Scenario constructor
    }

    private void initUI() {
        MapWindow w = new EditorWindow("resources/scenari/demo/scenario.json");
        add(w);

        pack();
        setTitle("Traffic2D: " + w.getMapView().getSceneName());
        //setSize(600,500);
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