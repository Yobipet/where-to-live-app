/*
Class: Menu
Author: Samdine Murray, Dylan Potter, Henry Sanders, Nole Liu
Created: 10/6/2023
Last Modified: 11/2/2023

Purpose:

Attributes: -finishflag: boolean
            -startFlag: boolean
            -menu: Frame

Methods: +pressStart(): void
         +pressMap(): void
         +pressCredits(): void
         +pressBack(): void
         +pressExit(): void
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

public class Menu {
    private boolean finishFlag = false;
    private boolean startFlag = false;
    private boolean mapFlag = false;
    private Frame menu = new Frame();
    Menu() {
        JFrame f = new JFrame();
        f.setSize(1600,800);
        JLabel l1 = new JLabel("Where To Live App");
        l1.setBounds(0, 100, 1600, 50);
        l1.setFont(new Font("Serif",Font.PLAIN,20));
        l1.setHorizontalAlignment(l1.CENTER);
        f.add(l1);
        JButton b1 = new JButton("START");
        b1.setBounds(750, 200, 100, 50);
        f.add(b1);
        JButton b2 = new JButton("MAP");
        b2.setBounds(750, 300, 100, 50);
        f.add(b2);
        JButton b3 = new JButton("CREDITS");
        b3.setBounds(750, 400, 100, 50);
        f.add(b3);
        JButton b4 = new JButton("EXIT");
        b4.setBounds(750, 500, 100, 50);
        f.add(b4);
        f.setLayout(null);
        f.setVisible(true);
        b1.addActionListener(this::pressStart);
        b2.addActionListener(this::pressMap);
        b3.addActionListener(this::pressCredits);
        b4.addActionListener(this::pressExit);
    }
    public void pressStart(ActionEvent e) {
        startFlag = true;
    }
    public void pressMap(ActionEvent e) {
        mapFlag = true;
    }
    public void pressCredits(ActionEvent e) {
        menu.setSize(1600,800);
        menu.show();
        Button b5 = new Button("BACK");
        b5.setBounds(100, 100, 400, 50);
        menu.add(b5);
        b5.addActionListener(this::pressBack);
    }
    public void pressBack(ActionEvent e) {
        menu.hide();
    }
    public void pressExit(ActionEvent e) {
        finishFlag = true;
    }

    // SETTERS & GETTERS
    public boolean getFinishFlag() {
        return finishFlag;
    }
    public boolean getStartFlag() {
        return startFlag;
    }
    public boolean getMapFlag() {return mapFlag;}
    public void setStartFlag(boolean startFlag) {
        this.startFlag = startFlag;
    }
    public void setMapFlag(boolean mapFlag) {
        this.mapFlag = mapFlag;
    }
}