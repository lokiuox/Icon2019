package com.patterson.ui.editor;

import com.patterson.ui.MapControls;
import com.patterson.ui.MapWindow;

import javax.swing.*;
import java.awt.*;

public class EditorWindow extends MapWindow {
    public static Dimension BUTTON_SPACING = new Dimension(5, 0);

    public EditorWindow() {
        super(new MapEditorView());
        init();
    }

    public EditorWindow(String json) {
        super(new MapEditorView(json));
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
        JButton auto = new JButton("Auto");
        def_card.add(auto);
        def_card.add(Box.createHorizontalGlue());
        JButton play = new JButton("Play");
        def_card.add(play);
        def_card.add(Box.createRigidArea(BUTTON_SPACING));
        this.addControls("selector", bottom, BorderLayout.PAGE_END);

        play.addActionListener(e -> switchPlayPause(play));
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
}
