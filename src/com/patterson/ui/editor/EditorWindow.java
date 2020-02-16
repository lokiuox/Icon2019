package com.patterson.ui.editor;

import com.patterson.ui.MapControls;
import com.patterson.ui.MapWindow;
import com.patterson.world.Scenario;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class EditorWindow extends MapWindow {
    private static Dimension BUTTON_SPACING = new Dimension(5, 0);

    public EditorWindow() {
        super(new MapEditorView());
        init();
    }

    public EditorWindow(String json) {
        super(new MapEditorView(json));
        init();
    }

    public EditorWindow(Scenario s) {
        super(new MapEditorView(s));
        init();
    }

    @Override
    protected void init() {
        getMapView().pause();
        MapControls bottom = new MapControls(getMapView());
        JPanel def_card = bottom.getCard("default");

        def_card.setLayout(new BoxLayout(def_card, BoxLayout.X_AXIS));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 3);

        def_card.add(Box.createRigidArea(BUTTON_SPACING));
        JButton strade = new JButton("Strade");
        def_card.add(strade);
        def_card.add(Box.createRigidArea(BUTTON_SPACING));
        JButton incroci = new JButton("Incroci");
        def_card.add(incroci);
        def_card.add(Box.createRigidArea(BUTTON_SPACING));
        JButton info = new JButton("Info");
        def_card.add(info);
        def_card.add(Box.createHorizontalGlue());
        JButton play = new JButton("Play");
        def_card.add(play);
        def_card.add(Box.createRigidArea(BUTTON_SPACING));
        JButton reset = new JButton("Reset");
        def_card.add(reset);
        def_card.add(Box.createRigidArea(BUTTON_SPACING));
        JButton save = new JButton("Salva");
        def_card.add(save);
        def_card.add(Box.createRigidArea(BUTTON_SPACING));
        this.addControls("selector", bottom, BorderLayout.PAGE_END);

        strade.addActionListener(e -> {
            MapEditorView editor = (MapEditorView) mapView;
            editor.activateMode("RoadDesign");
        });

        incroci.addActionListener(e -> {
            MapEditorView editor = (MapEditorView) mapView;
            editor.activateMode("IntersectionDesign");
        });

        info.addActionListener(e -> {
            MapEditorView editor = (MapEditorView) mapView;
            editor.activateMode("InfoMode");
        });

        reset.addActionListener(e -> reset(play));
        play.addActionListener(e -> switchPlayPause(play));
        save.addActionListener(e -> saveScenario());

    }

    private void switchPlayPause(JButton b) {
        if (b.getText().equals("Play")) {
            b.setText("Pausa");
            mapView.resume();
        } else {
            b.setText("Play");
            mapView.pause();
        }
    }

    private void reset(JButton play_button) {
        int choice = JOptionPane.showConfirmDialog(this,
                "Resettando lo scenario, tutte le modifiche non salvate andranno perse.\n" +
                "Continuare?",
                "Conferma Reset Scenario",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.NO_OPTION) {
            return;
        }
        String json = mapView.getScenario().getJSONPath();
        if (json == null) {
            mapView.setScenario(new Scenario());
        } else {
            mapView.setScenario(new Scenario(json));
        }
        mapView.actionPerformed(null);
        mapView.pause();
        play_button.setText("Play");
    }

    private File chooseFolder(String starting_path) {
        File save_folder = null;
        JFileChooser fc = new JFileChooser(starting_path);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setSelectedFile(null);
        fc.setFileFilter(new DirectoryFilter());
        while (save_folder == null) {
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                save_folder = fc.getSelectedFile();
                if (!save_folder.exists()) {
                    save_folder.mkdir();
                } else if (save_folder.listFiles().length != 0) {
                    int choice = JOptionPane.showConfirmDialog(this,
                            "La cartella selezionata non è vuota.\n" +
                                    "Continuare comunque?",
                            "Cartella selezionata non vuota",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (choice == JOptionPane.NO_OPTION) {
                        save_folder = null;
                    }
                }
            } else {
                //User cancelled the action
                return null;
            }
        }
        return save_folder;
    }

    private void saveScenario() {
        String json = mapView.getScenario().getJSONPath();
        File save_folder;
        if (json == null) {
            String name = (String) JOptionPane.showInputDialog(this,
                    "Inserisci un nome per il nuovo scenario:",
                    "Nome Scenario",
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    null,
                    "NuovoScenario");
            if (name == null) return; //User cancelled the action
            mapView.getScenario().setName(name);
            save_folder = chooseFolder("resources/scenari");
        } else {
            save_folder = new File(json).getParentFile();
            int choice = JOptionPane.showConfirmDialog(this,
                    "Sovrascrivere lo scenario corrente?",
                    "Scenario già esistente",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                //Do nothing
            } else if (choice == JOptionPane.NO_OPTION) {
                save_folder = chooseFolder(save_folder.getPath());
            } else if (choice == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        mapView.getScenario().exportJSON(new File(save_folder, "scenario.json").getPath());
        //mapView.getScenario().exportKB(new File(save_folder, "KB.pl").getPath());
    }

    private class DirectoryFilter extends FileFilter {
        //Accept all directories and all gif, jpg, tiff, or png files.
        public boolean accept(File f) {
            return f.isDirectory() || !f.exists();
        }

        //The description of this filter
        public String getDescription() {
            return "Cartelle";
        }
    }
}
