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
        f.setSize(1920,1080);
        Label l1 = new Label("Where To Live App");
        l1.setBounds(100, 100, 100, 50);
        f.add(l1);
        Button b1 = new Button("START");
        b1.setBounds(100, 200, 100, 50);
        f.add(b1);
        Button b2 = new Button("MAP");
        b2.setBounds(100, 300, 100, 50);
        f.add(b2);
        Button b3 = new Button("CREDITS");
        b3.setBounds(100, 400, 100, 50);
        f.add(b3);
        Button b4 = new Button("EXIT");
        b4.setBounds(100, 500, 100, 50);
        f.add(b4);
        f.setLayout(null);
        f.setVisible(true);
        b1.addActionListener(this::pressStart);
        b4.addActionListener(this::pressExit);
    }
    public void pressStart(ActionEvent e) {
        new Questionnaire();
    }
    public void pressMap() {

    }
    public void pressCredits() {

    }
    public void pressExit(ActionEvent e) {
        System.exit(0);
    }
}
