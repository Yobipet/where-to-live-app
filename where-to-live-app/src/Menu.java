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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Menu {
    private boolean finishFlag = false;
    private boolean startFlag = false;
    private boolean mapFlag = false;
    private JFrame credits;
    private File sources = new File("RegionDatabase.csv");
    Menu() {
        Frame f = new Frame();
        f.setSize(1600,800);
        JLabel l1 = new JLabel("Where To Live App");
        l1.setFont(new Font("Serif",Font.PLAIN,25));
        l1.setHorizontalAlignment(l1.CENTER);
        l1.setBounds(550, 100, 500, 50);
        f.add(l1);
        Button b1 = new Button("START");
        b1.setBounds(750, 200, 100, 50);
        f.add(b1);
        Button b2 = new Button("MAP");
        b2.setBounds(750, 300, 100, 50);
        f.add(b2);
        Button b3 = new Button("CREDITS");
        b3.setBounds(750, 400, 100, 50);
        f.add(b3);
        Button b4 = new Button("EXIT");
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
        credits = new JFrame("Credits");
        credits.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        credits.setSize(1600,800);
        JLabel cL1 = new JLabel("<html><b>Credits</b></html>");
        cL1.setBounds(700, 50, 200, 50);
        cL1.setHorizontalAlignment(JLabel.CENTER);
        credits.add(cL1);
        JLabel cL2 = new JLabel("<html><u>Development Team</u></html>");
        cL2.setBounds(700, 100, 200, 50);
        cL2.setHorizontalAlignment(JLabel.CENTER);
        credits.add(cL2);
        JLabel cL3 = new JLabel("Henry Sanders          Nole Liu          Samdine Murray          Dylan Potter");
        cL3.setBounds(500, 150, 600, 50);
        cL3.setHorizontalAlignment(JLabel.CENTER);
        credits.add(cL3);
        JLabel cL4 = new JLabel("<html><u>Database Sources</u></html>");
        cL4.setBounds(700, 200, 200, 50);
        cL4.setHorizontalAlignment(JLabel.CENTER);
        credits.add(cL4);
        int tempY = 250;
        int tempX = 200;
        for (int i = 103; i <= 105; i++) {
            String[] tempStr = getSource(i);
            for (int j = 1; j < tempStr.length; j++) {
                if (tempStr[j] != null && tempStr[j] != ""){
                    JLabel tempL = new JLabel(tempStr[j]);
                    tempL.setBounds(tempX, tempY, 400, 50);
                    tempL.setHorizontalAlignment(JLabel.CENTER);
                    credits.add(tempL);
                    tempY += 50;
                    if (tempY >= 700){
                        tempY = 250;
                        tempX += 800;
                    }
                }else{
                }
            }
        }
        JButton cB1 = new JButton("BACK");
        cB1.setBounds(600, 700, 400, 50);
        credits.add(cB1);
        cB1.addActionListener(this::pressCreditsBack);
        credits.setLayout(null);
        credits.setVisible(true);
    }
    public void pressCreditsBack(ActionEvent e) {
        credits.hide();
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
    public void setStartFlag(boolean startFlag) {
        this.startFlag = startFlag;
    }
    public String[] getSource(int lineNum) {
        String row = "";
        String[] line = new String[20];
        try {
            FileReader fr = new FileReader(sources);
            BufferedReader br = new BufferedReader(fr);
            for(int i = 0; i < lineNum; i++) {
                row = br.readLine();
                for (int j = 0; j < row.length(); j++){
                    line = row.split(",");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }
    public boolean getMapFlag() {return mapFlag;}
    public void setMapFlag(boolean mapFlag) {
        this.mapFlag = mapFlag;
    }
}