/*
Class: Manager
Author: Samdine Murray, Dylan Potter, Henry Sanders, Nole Liu
Created: 10/2/2023
Last Modified: 11/2/2023

Purpose: This class contains most of the calculation methods and is the central class that communicates
between all classes.

Attributes: -tierList: int[]
            -data: MapData()
            -regionFile: File
            -database: ArrayList<ArrayList<String>>()
            -answers: ArrayList<String>()
            -questionnaire: Questionnaire
            -finishflag: boolean
            -startFlag: boolean
            -qFlag: boolean

Methods: +formatUserAnswers(): void
         +parseDatabase(): void
         +runProgram(): void
         +createQuestionnaire: void
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Manager {
    // Attributes
    private ArrayList<ArrayList<Double>> matchPercentages = new ArrayList<>();
    private ArrayList<Integer> tierList = new ArrayList<>();
    private File regionFile = new File("RegionDatabase.csv");
    private File statesFile = new File("StateNames.csv");
    private ArrayList<ArrayList<String>> database = new ArrayList<>();
    private ArrayList<String> stateNames = new ArrayList<>();
    private ArrayList<String> answers;
    private boolean qFlag;
    private boolean mapFlag = false;

    // Methods
    public void runProgram() {
        parseDatabases();
        this.qFlag = true;
        Menu menu = new Menu();
        while (!menu.getFinishFlag()) {
            if (menu.getStartFlag()) {
                createQuestionnaire();
                menu.setStartFlag(false);
            }
            if (mapFlag) {
                if (!qFlag) {
                    matchPercentages = new ArrayList<>();
                    formatUserAnswers();
                    createTierLevels();
                    createMap();
                }
                else {
                    JFrame mapErrorWindow = new JFrame();
                    JPanel mapErrorPanel = new JPanel(new BorderLayout());
                    JLabel errorMessage = new JLabel("Please complete the questionnaire first.");
                    errorMessage.setFont(new Font("Serif",Font.PLAIN,25));
                    errorMessage.setHorizontalAlignment(errorMessage.CENTER);
                    mapErrorPanel.add(errorMessage, BorderLayout.CENTER);
                    JButton backButton = new JButton("Back");
                    mapErrorPanel.add(backButton, BorderLayout.SOUTH);
                    backButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            mapErrorWindow.hide();
                        }
                    });
                    mapErrorWindow.add(mapErrorPanel);
                    mapErrorWindow.setPreferredSize(new Dimension(600,600));
                    mapErrorWindow.pack();
                    mapErrorWindow.setVisible(true);
                }
                menu.setMapFlag(false);
            }
            mapFlag = menu.getMapFlag();
        }
        System.out.println(answers);
        System.exit(0);
    }
    public void parseDatabases() {
        try {
            FileReader fr = new FileReader(regionFile);
            BufferedReader br = new BufferedReader(fr);
            // System.out.println("Began parsing database.");
            String titleRow = br.readLine();
            for (int i = 0;i < 100; i++) {
                String row = br.readLine();
                String[] cols = row.split(",");
                database.add(new ArrayList<>());
                String stateData = "";
                for (int j = 0;j < cols.length;j++) {
                    database.get(i).add(cols[j]);
                    stateData = String.format(stateData + " " + database.get(i).get(j));
                }
                // System.out.println(stateData);
            }
            fr = new FileReader(statesFile);
            br = new BufferedReader(fr);
            for (int i = 0; i < 50; i++) {
                stateNames.add(br.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createQuestionnaire() {
        Questionnaire questionnaire = new Questionnaire();
        int i = 0;
        qFlag = true;
        mapFlag = false;
        answers = null;
        while (qFlag) {
            qFlag = questionnaire.getQStatus();
            try {
                Thread.sleep(30);
            } catch (Exception e) {}
        }
        mapFlag = questionnaire.getMapFlag();
        answers = questionnaire.getAnswers();
    }
    public void createMap() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        JFrame mapFrame = new JFrame();
        Map map = new Map(tierList,stateNames,database);
        mapFrame.add(map);
        mapFrame.pack();
        mapFrame.setLocationRelativeTo(null);
        mapFrame.setVisible(true);
        boolean backFlag = false;
        while (!backFlag) {
            backFlag = map.getBackFlag();
            try {
                Thread.sleep(30);
            } catch (Exception e) {}
        }
        mapFrame.hide();
    }
    public void formatUserAnswers() {
        for (int i = 0; i < 100; i++) {
            ArrayList<Double> tempArray = new ArrayList<>();
            tempArray.add(0 - ((Double.parseDouble(answers.get(0)) - Double.parseDouble(database.get(i).get(2)))/Double.parseDouble(answers.get(0))));
            if (tempArray.get(0) < 0) {
                tempArray.set(0,0.0);
            }
            tempArray.add(0 - ((Double.parseDouble(answers.get(1)) - Double.parseDouble(database.get(i).get(3)))/Double.parseDouble(answers.get(1))));
            if (tempArray.get(1) < 0) {
                tempArray.set(1,0.0);
            }
            tempArray.add(Math.abs((Double.parseDouble(answers.get(2)) - Double.parseDouble(database.get(i).get(4))))/Double.parseDouble(answers.get(2)));
            tempArray.add(Math.abs((Double.parseDouble(answers.get(3)) - Double.parseDouble(database.get(i).get(5))))/Double.parseDouble(answers.get(3)));
            if (answers.get(4).contains("Yes")) {
                tempArray.add((100.0 - Double.parseDouble(database.get(i).get(8)))/100.0);
            }
            else {
                tempArray.add(0.0);
            }
            if (answers.get(5).contains("No")) {
                tempArray.add((0.0 + Double.parseDouble(database.get(i).get(9)))/10);
                if (tempArray.get(5) < .6) {
                    tempArray.set(5,0.0);
                }
            }
            else {
                tempArray.add(0.0);
            }
            double popDensityVal = Double.parseDouble(database.get(i).get(10));
            if (answers.get(6).contains("Abstain")) {
                tempArray.add(0.0);
            }
            if (answers.get(6).contains("Very Low")) {
                if (popDensityVal <= 114.3) {
                    tempArray.add(0.0);
                }
                else if (popDensityVal > 114.3 && popDensityVal <= 261.8) {
                    tempArray.add(0.5);
                }
                else if (popDensityVal > 261.8 && popDensityVal <= 482.5) {
                    tempArray.add(1.0);
                }
                else if (popDensityVal > 482.5 && popDensityVal <= 1692.4) {
                    tempArray.add(1.5);
                }
                else if (popDensityVal > 1692.4) {
                    tempArray.add(2.0);
                }
            }
            if (answers.get(6).contains("Low") && !answers.get(6).contains("Very")) {
                if (popDensityVal <= 114.3) {
                    tempArray.add(0.5);
                }
                else if (popDensityVal > 114.3 && popDensityVal <= 261.8) {
                    tempArray.add(0.0);
                }
                else if (popDensityVal > 261.8 && popDensityVal <= 482.5) {
                    tempArray.add(0.5);
                }
                else if (popDensityVal > 482.5 && popDensityVal <= 1692.4) {
                    tempArray.add(1.0);
                }
                else if (popDensityVal > 1692.4) {
                    tempArray.add(1.5);
                }
            }
            if (answers.get(6).contains("Average")) {
                if (popDensityVal <= 114.3) {
                    tempArray.add(1.0);
                }
                else if (popDensityVal > 114.3 && popDensityVal <= 261.8) {
                    tempArray.add(0.5);
                }
                else if (popDensityVal > 261.8 && popDensityVal <= 482.5) {
                    tempArray.add(0.0);
                }
                else if (popDensityVal > 482.5 && popDensityVal <= 1692.4) {
                    tempArray.add(0.5);
                }
                else if (popDensityVal > 1692.4) {
                    tempArray.add(1.0);
                }
            }
            if (answers.get(6).contains("High") && !answers.get(6).contains("Very")) {
                if (popDensityVal <= 114.3) {
                    tempArray.add(1.5);
                }
                else if (popDensityVal > 114.3 && popDensityVal <= 261.8) {
                    tempArray.add(1.0);
                }
                else if (popDensityVal > 261.8 && popDensityVal <= 482.5) {
                    tempArray.add(0.5);
                }
                else if (popDensityVal > 482.5 && popDensityVal <= 1692.4) {
                    tempArray.add(0.0);
                }
                else if (popDensityVal > 1692.4) {
                    tempArray.add(0.5);
                }
            }
            if (answers.get(6).contains("Very High")) {
                if (popDensityVal <= 114.3) {
                    tempArray.add(2.0);
                }
                else if (popDensityVal > 114.3 && popDensityVal <= 261.8) {
                    tempArray.add(1.5);
                }
                else if (popDensityVal > 261.8 && popDensityVal <= 482.5) {
                    tempArray.add(1.0);
                }
                else if (popDensityVal > 482.5 && popDensityVal <= 1692.4) {
                    tempArray.add(0.5);
                }
                else if (popDensityVal > 1692.4) {
                    tempArray.add(0.0);
                }
            }
            if (answers.get(7).contains("Yes")) {
                tempArray.add((0.0 + Double.parseDouble(database.get(i).get(12)))/20000);
            }
            else {
                tempArray.add(0.0);
            }
            if (answers.get(8).contains("Abstain")) {
                tempArray.add(0.0);
            }
            else {
                tempArray.add(Math.abs(Double.parseDouble(answers.get(8)) - Double.parseDouble(database.get(i).get(13))) / 5);
            }
            if (answers.get(9).contains("Yes")) {
                tempArray.add(Math.abs((10/Double.parseDouble(database.get(i).get(14)))-0.127));
            }
            else {
                tempArray.add(0.0);
            }
            if (answers.get(10).contains("Abstain")) {
                tempArray.add(0.0);
            }
            else {
                tempArray.add(Math.abs(Double.parseDouble(answers.get(10)) - Double.parseDouble(database.get(i).get(16))) / 2.5);
            }
            if (answers.get(11).contains("Abstain")) {
                tempArray.add(0.0);
            }
            else if (answers.get(11).contains("Medicinally")) {
                tempArray.add(Math.abs((Double.parseDouble(database.get(i).get(18))/2)-1.0));
            }
            else if (answers.get(11).contains("Recreationally")) {
                tempArray.add(Math.abs((Double.parseDouble(database.get(i).get(17))/2)-0.75));
            }
            else {
                tempArray.add(Math.abs((Double.parseDouble(database.get(i).get(18))/2)));
            }
            if (answers.get(12).contains("null") || answers.get(13).isEmpty()) {
                tempArray.add(0.0);
            }
            else {
                tempArray.add(Math.abs(Double.parseDouble(answers.get(12))-Double.parseDouble(database.get(i).get(15)))/100);
            }
            if (answers.get(13).contains("null") || answers.get(13).isEmpty()) {
                tempArray.add(0.0);
            }
            else if (Double.parseDouble(answers.get(13)) < Double.parseDouble(database.get(i).get(6))) {
                tempArray.add(Math.abs(Double.parseDouble(answers.get(13))-Double.parseDouble(database.get(i).get(6)))/1000000);
            }
            else {
                tempArray.add(0.0);
            }
            System.out.println(database.get(i).get(0) + ", " + database.get(i).get(1) + " " + tempArray);
            matchPercentages.add(tempArray);
        }
    }
    public void createTierLevels() {
        tierList = new ArrayList<>();
        for (int i = 0; i < matchPercentages.size(); i++) {
            double percentsAdded = 0.0;
            for (int j = 0; j < matchPercentages.get(i).size(); j++) {
                percentsAdded += matchPercentages.get(i).get(j);
            }
            percentsAdded = (percentsAdded / matchPercentages.get(i).size());
            if (percentsAdded < .15) {
                tierList.add(5);
            }
            else if (percentsAdded > .15 && percentsAdded < .2) {
                tierList.add(4);
            }
            else if (percentsAdded > .2 && percentsAdded < .25) {
                tierList.add(3);
            }
            else if (percentsAdded > .25 && percentsAdded < .3) {
                tierList.add(2);
            }
            else {
                tierList.add(1);
            }
            System.out.println(percentsAdded + " " + tierList.get(i));
        }
    }
}
