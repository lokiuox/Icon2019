package com.patterson;


import com.patterson.world.Surface;

import javax.swing.*;
import java.awt.*;


public class AppMain extends JFrame {

    public AppMain() {
        initUI();
    }

    private void initUI() {

        add(new Surface());

        pack();
        setTitle("Traffic2D");
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