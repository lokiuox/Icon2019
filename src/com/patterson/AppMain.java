package com.patterson;

import com.patterson.ui.MapWindow;
import com.patterson.ui.editor.EditorWindow;
import com.patterson.world.DemoScenario;

import javax.swing.*;
import java.awt.*;


public class AppMain extends JFrame {

    public AppMain() {
        initUI();
        //KnowledgeBase.init("resources/KB.pl"); //Is this needed? We can init from the Scenario constructor
    }

    private void initUI() {
        //MapWindow w = new EditorWindow("resources/scenari/demo/scenario.json");
        //MapWindow w = new EditorWindow();
        //MapWindow w = new MapWindow(DemoScenario.getScenario());
        MapWindow w = new MapWindow("resources/scenari/partial/scenario.json");
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