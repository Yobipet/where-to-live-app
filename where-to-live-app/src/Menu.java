/*
Class: Menu
Author: Samdine Murray, Dylan Potter, Henry Sanders, Nole Liu
Created: 10/6/2023
Last Modified: 10/6/2023

Purpose:

Attributes:

Methods: +pressStart(): void
         +pressMap(): void
         +pressCredits(): void
 */
import java.awt.event.*;
import java.awt.*;

public class Menu {
    Menu() {
        Frame f = new Frame();
        f.setSize(1600,800);
        Label l1 = new Label("Where To Live App");
        l1.setBounds(700, 100, 100, 50);
        f.add(l1);
        Button b1 = new Button("START");
        b1.setBounds(700, 200, 100, 50);
        f.add(b1);
        Button b2 = new Button("MAP");
        b2.setBounds(700, 300, 100, 50);
        f.add(b2);
        Button b3 = new Button("CREDITS");
        b3.setBounds(700, 400, 100, 50);
        f.add(b3);
        Button b4 = new Button("EXIT");
        b4.setBounds(700, 500, 100, 50);
        f.add(b4);
        f.setLayout(null);
        f.setVisible(true);
        b1.addActionListener(this::pressStart);
        b2.addActionListener(this::pressMap);
        b3.addActionListener(this::pressCredits);
        b4.addActionListener(this::pressExit);
    }
    public void pressStart(ActionEvent e) {
        new Questionnaire();
    }
    public void pressMap(ActionEvent e) {
        new Map();
    }
    Frame c = new Frame();
    public void pressCredits(ActionEvent e) {
        c.setSize(1600,800);
        c.show();
        Button b5 = new Button("BACK");
        b5.setBounds(100, 100, 400, 50);
        c.add(b5);
        b5.addActionListener(this::pressBack);
    }
    public void pressBack(ActionEvent e) {
        c.hide();
    }
    public void pressExit(ActionEvent e) {
        System.exit(0);
    }
}
