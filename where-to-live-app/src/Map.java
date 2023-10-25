import java.awt.*;
import java.awt.event.ActionEvent;

/*
Class: Map
Author: Samdine Murray, Dylan Potter, Henry Sanders, Nole Liu
Created: 10/6/2023
Last Modified: 10/6/2023

Purpose:

Attributes:

Methods: +selectMapMode(): void
         +selectState(): void
 */
public class Map {
    Frame m = new Frame();
    Map(){
        m.setSize(1600,800);
        m.show();
        Label l1 = new Label("Map");
        l1.setBounds(100, 100, 100, 50);
        m.add(l1);
        Button b1 = new Button("BACK");
        b1.setBounds(1400, 700, 400, 50);
        m.add(b1);
        b1.addActionListener(this::pressBack);
    }
    private void pressBack(ActionEvent e) {
        m.hide();
    }
    public void selectMapMode() {

    }
    public void selectState() {

    }
}
