package com.patterson.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import java.awt.*;


public class MapControls extends JPanel {
    MapView mapView;
    Map<String, JPanel> cards = new HashMap<>();

    public MapControls(MapView m) {
        super(new CardLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JPanel default_card = new JPanel();
        cards.put("default", default_card);
        this.add(default_card);
        mapView = m;
    }

    public MapControls(MapView m, Dimension d) {
        this(m);
        this.setPreferredSize(d);
    }

    public void setPreferredSize(int w, int h) {
        this.setPreferredSize(new Dimension(w, h));
    }

    public JPanel getCard(String id) {
        return cards.get(id);
    }

    public void addCard(JPanel card, String id) {
        cards.put(id, card);
        this.add(card, id);
    }

    public Set<String> getAvailableCards() {
        return cards.keySet();
    }

    public void selectCard(String id) {
        CardLayout layout = (CardLayout) this.getLayout();
        layout.show(this, id);
    }

}
