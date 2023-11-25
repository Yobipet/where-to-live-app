/*
Class: PopUpWindow
Author: Samdine Murray, Dylan Potter, Henry Sanders, Nole Liu
Created: 11/22/2023
Last Modified: 11/22/2023

Purpose: Creates the pop-up window when a state/county is clicked on with the pros and cons displayed.

Attributes:

Methods:
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.geom.Path2D;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class PopUpWindow {
    private JFrame popup = new JFrame();
    private File regionFile = new File("RegionDatabase.csv"); // Information for each state
    private File coordinates = new File("CoordinateData.xlsx");
    private int rowOfLocation;
    public PopUpWindow(int rowOfLocation) {     // Initialize the correct row of the information
        this.rowOfLocation = rowOfLocation;
        popup.setSize(1600,800);
        popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        popup.setLayout(null);
        popup.setVisible(true);
    }
    public void populateWindow() {  // takes the rows that the state/county is in the spreadsheet as the input
        String row = "";
        String[] cols;
        try {
            FileReader fr = new FileReader(regionFile);
            BufferedReader br = new BufferedReader(fr);
            String headers = br.readLine();
            String[] headerComponents = headers.split(",");
            for(int counter = 0; counter < rowOfLocation; counter++) { // For loop reads down to the line that the information is at in the spreadsheet
                row = br.readLine();
            }
            cols = row.split(",");  // When correct line is reached, split the information by column
            for(int i = 0; i < cols.length; i++) {  // For however much information there is, put it onto the window
                if(i >= 17) {   // This if statement changes weed statistics from 0,1 to Legal, Illegal
                    switch(Integer.parseInt(cols[i])) {
                        case 0: cols[i] = "Illegal";
                        case 1: cols[i] = "Legal";
                    }
                }
                JLabel information = new JLabel(headerComponents[i] + ": " + cols[i]);
                information.setBounds(20, i * (800 / 20), 1600, 50);
                popup.add(information);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // This is to put into the map component class, it's just here for now cause im dumb and realized that this doesn't pertain to my class.
    public void createButtons() {
        int numOfRows = 100;    // able to change for the number of rows in a spreadsheet, idk really know how to get the number of rows with a command
        int buttonHeight = 1;
        int buttonWidth = 1;
        int buttonX;    // excel sheet can from the first column name -> java x coordinate -> java y coordinate
        int buttonY;
        try {
            FileReader fr = new FileReader(coordinates);
            BufferedReader br = new BufferedReader(fr);
            for(int i = 0; i < numOfRows; i++) {
                String row = br.readLine();
                String[] cols = row.split(",");
                buttonX = Integer.parseInt(cols[1]);
                buttonY = Integer.parseInt(cols[2]);
                JButton locationName = new JButton(cols[0]);
                locationName.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
