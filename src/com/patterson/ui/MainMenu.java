package com.patterson.ui;

import com.patterson.ui.editor.EditorWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainMenu extends JFrame {
    private static MainMenu menu;
    private FrameClosingInterceptor frameCloseInterceptor = new FrameClosingInterceptor();
    private JPanel main;
    MainMenu() {
        menu = this;
        main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
        this.setContentPane(main);

        this.setTitle("Progetto ICon 19/20 - Gruppo Patterson");

        //Label

        main.add(Box.createRigidArea(new Dimension(0, 20)));

        //Combo box scenari
        main.add(Box.createVerticalGlue());

        List<String> scenari = getScenari();
        JComboBox<String> scenari_selector = new JComboBox<>(scenari.toArray(new String[0]));
        //scenari_selector.setSize(new Dimension(200, 2));
        scenari_selector.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        main.add(scenari_selector);
        main.add(Box.createVerticalGlue());

        main.add(Box.createRigidArea(new Dimension(0, 20)));

        //Pulsante simulazione
        JButton start_simulation = new JButton("Simulazione");
        start_simulation.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(start_simulation);
        main.add(Box.createRigidArea(new Dimension(0, 8)));

        //Pulasnte editor
        JButton start_editor = new JButton("Editor");
        start_editor.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(start_editor);
        main.add(Box.createRigidArea(new Dimension(0, 8)));

        //Pulsante nuovo
        JButton new_scenario = new JButton("Crea Nuovo");
        new_scenario.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(new_scenario);
        main.add(Box.createRigidArea(new Dimension(0, 20)));
        main.add(Box.createVerticalGlue());

        start_simulation.addActionListener(e -> {
            String json_path = "resources/scenari/" + (String) scenari_selector.getSelectedItem() + "/scenario.json";
            MapWindow m = new MapWindow(json_path);
            m.addWindowListener(frameCloseInterceptor);
            m.setVisible(true);
            this.setVisible(false);
        });

        start_editor.addActionListener(e -> {
            String json_path = "resources/scenari/" + (String) scenari_selector.getSelectedItem() + "/scenario.json";
            MapWindow m = new EditorWindow(json_path);
            m.addWindowListener(frameCloseInterceptor);
            m.setVisible(true);
            this.setVisible(false);
        });

        new_scenario.addActionListener(e -> {
            MapWindow m = new EditorWindow();
            m.addWindowListener(frameCloseInterceptor);
            m.setVisible(true);
            this.setVisible(false);
        });

        //main.setSize(new Dimension(200, 300));
        pack();
        this.setSize(new Dimension(300, 230));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private List<String> getScenari() {
        File basePath = new File("resources/scenari");
        File[] scenari = basePath.listFiles(new ScenarioFilter());
        List<String> s = new ArrayList<>();
        for (File f: scenari) {
            s.add(f.getName());
        }
        return s;
    }

    public static void showMenu() {
        menu.setVisible(true);
    }

    private class FrameClosingInterceptor extends WindowAdapter {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            showMenu();
        }
    }

    public static void main(String[] args) {
        MainMenu m = new MainMenu();
        m.setVisible(true);
    }

    private class ScenarioFilter implements FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                List<String> childen = Arrays.asList(f.list());
                if (childen.contains("scenario.json") && childen.contains("KB.pl")) {
                    return true;
                }
            }
            return false;
        }
    }
}
