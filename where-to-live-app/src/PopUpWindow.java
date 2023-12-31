/*
Class: PopUpWindow
Author: Samdine Murray, Dylan Potter, Henry Sanders, Nole Liu
Created: 11/22/2023
Last Modified: 11/25/2023

Purpose: Creates the pop-up window when a state/county is clicked on with the pros and cons displayed. Change
some values of the database to plain English in the window.

Attributes: -popup: JFrame
            -regionFile: File
            -coordinates: File
            -rowOfLocation: int

Methods:    +populateWindow(): void
            +changeReadability(int i, String[] cols): String[]
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class PopUpWindow {
    private JPanel popup = new JPanel();
    private File regionFile = new File("RegionDatabase.csv"); // Information for each state
//    private File coordinates = new File("StateCoordinates.csv"); // This goes with the createButtons method
    private int rowOfLocation;
    public PopUpWindow(int rowOfLocation) {     // Initialize the correct row of the information
        this.rowOfLocation = rowOfLocation;
        JFrame popUpWindow = new JFrame();
        popUpWindow.setSize(400, 900);
        popup.setLayout(new BorderLayout());
        popup.setSize(400,900);
        populateWindow();
        JButton backButton = new JButton("BACK");
        backButton.setBounds(1400, 700, 100, 50);
        popup.add(backButton, BorderLayout.SOUTH);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popUpWindow.hide();
            }
        });
        popUpWindow.add(popup);
        popUpWindow.setVisible(true);
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
                cols = changeReadability(i, cols);
                JLabel information = new JLabel(headerComponents[i] + ": " + cols[i]);
                information.setBounds(20, i * (800 / 20), 1600, 50);
                popup.add(information, BorderLayout.SOUTH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // This method converts number values from the database to plain english
    public String[] changeReadability(int i, String[] cols) {
        if (i == 13) {
            if(Integer.parseInt(cols[i]) == 1) {
                cols[i] = "Very Democratic";
            } else if(Integer.parseInt(cols[i]) == 2) {
                cols[i] = "Democratic";
            } else if(Integer.parseInt(cols[i]) == 3) {
                cols[i] = "Neutral";
            } else if(Integer.parseInt(cols[i]) == 4) {
                cols[i] = "Republican";
            } else {
                cols[i] = "Very Republican";
            }
        } else if (i == 16) {   // This if statement changes weed statistics from 0,1 to Legal, Illegal
            if(Integer.parseInt(cols[i]) == 1) {
                cols[i] = "Very Low";
            } else if(Integer.parseInt(cols[i]) == 2) {
                cols[i] = "Low";
            } else if(Integer.parseInt(cols[i]) == 3) {
                cols[i] = "Average";
            } else if(Integer.parseInt(cols[i]) == 4) {
                cols[i] = "High";
            } else {
                cols[i] = "Very High";
            }

        } else if(i >= 17) {
            if(Integer.parseInt(cols[i]) == 0) {
                cols[i] = "Illegal";
            } else if(Integer.parseInt(cols[i]) == 1){
                cols[i] = "Legal";
            } else {
                cols[i] = "Illegal, but Decriminalized";
            }
        }
        return cols;
    }

    // Getters and Setters
    public void setRowOfLocation(int rowOfLocation) {
        this.rowOfLocation = rowOfLocation;
    }
    public int getRowOfLocation() {
        return rowOfLocation;
    }
}