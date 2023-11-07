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

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Manager {
    // Attributes
    private ArrayList<ArrayList<Double>> matchPercentages = new ArrayList<>();
    private ArrayList<Integer> tierList = new ArrayList<>();
    private MapData data;
    private File regionFile = new File("RegionDatabase.csv");
    private ArrayList<ArrayList<String>> database = new ArrayList<>();
    private ArrayList<String> answers;
    private Questionnaire questionnaire;
    private boolean finishFlag = false;
    private boolean startFlag = false;
    private boolean qFlag = false;

    // Methods
    public void runProgram() {
        parseDatabase();
        Menu menu = new Menu();
        int i = 0;
        while (!finishFlag) {
            if (startFlag) {
                createQuestionnaire();
                menu.setStartFlag(false);
            }
//            System.out.println(answers);
            startFlag = menu.getStartFlag();
            finishFlag = menu.getFinishFlag();
        }
        System.out.println(answers);
//        System.out.println("Answer amt: " + answers.size());
//        testCase();
        System.exit(0);
    }
    public void parseDatabase() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createQuestionnaire() {
        questionnaire = new Questionnaire();
        int i = 0;
        qFlag = true;
        while (qFlag) {
            qFlag = questionnaire.getQStatus();
            try {
                Thread.sleep(30);
            } catch (Exception e) {}
        }
        answers = questionnaire.getAnswers();
        formatUserAnswers();
        createTierLevels();
    }
    public void formatUserAnswers() {
        for (int i = 0; i < 100; i++) {
            ArrayList<Double> tempArray = new ArrayList<>();
            tempArray.add((Double.parseDouble(answers.get(0)) - Double.parseDouble(database.get(i).get(2)))/Double.parseDouble(answers.get(0)));
            if (tempArray.get(0) < 0) {
                tempArray.set(0,0.0);
            }
            tempArray.add((Double.parseDouble(answers.get(1)) - Double.parseDouble(database.get(i).get(3)))/Double.parseDouble(answers.get(1)));
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
            if (answers.get(6).contains("Very Low")) {
                if (popDensityVal <= 114.3) {
                    tempArray.add(0.0);
                }
                else if (popDensityVal > 114.3 && popDensityVal <= 261.8) {
                    tempArray.add(0.25);
                }
                else if (popDensityVal > 261.8 && popDensityVal <= 482.5) {
                    tempArray.add(0.50);
                }
                else if (popDensityVal > 482.5 && popDensityVal <= 1692.4) {
                    tempArray.add(0.75);
                }
                else if (popDensityVal > 1692.4) {
                    tempArray.add(1.0);
                }
            }
            if (answers.get(6).contains("Low") && !answers.get(6).contains("Very")) {
                if (popDensityVal <= 114.3) {
                    tempArray.add(0.25);
                }
                else if (popDensityVal > 114.3 && popDensityVal <= 261.8) {
                    tempArray.add(0.0);
                }
                else if (popDensityVal > 261.8 && popDensityVal <= 482.5) {
                    tempArray.add(0.25);
                }
                else if (popDensityVal > 482.5 && popDensityVal <= 1692.4) {
                    tempArray.add(0.50);
                }
                else if (popDensityVal > 1692.4) {
                    tempArray.add(0.75);
                }
            }
            if (answers.get(6).contains("Average")) {
                if (popDensityVal <= 114.3) {
                    tempArray.add(0.50);
                }
                else if (popDensityVal > 114.3 && popDensityVal <= 261.8) {
                    tempArray.add(0.25);
                }
                else if (popDensityVal > 261.8 && popDensityVal <= 482.5) {
                    tempArray.add(0.0);
                }
                else if (popDensityVal > 482.5 && popDensityVal <= 1692.4) {
                    tempArray.add(0.25);
                }
                else if (popDensityVal > 1692.4) {
                    tempArray.add(0.50);
                }
            }
            if (answers.get(6).contains("High") && !answers.get(6).contains("Very")) {
                if (popDensityVal <= 114.3) {
                    tempArray.add(0.75);
                }
                else if (popDensityVal > 114.3 && popDensityVal <= 261.8) {
                    tempArray.add(0.50);
                }
                else if (popDensityVal > 261.8 && popDensityVal <= 482.5) {
                    tempArray.add(0.25);
                }
                else if (popDensityVal > 482.5 && popDensityVal <= 1692.4) {
                    tempArray.add(0.0);
                }
                else if (popDensityVal > 1692.4) {
                    tempArray.add(0.25);
                }
            }
            if (answers.get(6).contains("Very High")) {
                if (popDensityVal <= 114.3) {
                    tempArray.add(1.0);
                }
                else if (popDensityVal > 114.3 && popDensityVal <= 261.8) {
                    tempArray.add(0.75);
                }
                else if (popDensityVal > 261.8 && popDensityVal <= 482.5) {
                    tempArray.add(0.50);
                }
                else if (popDensityVal > 482.5 && popDensityVal <= 1692.4) {
                    tempArray.add(0.25);
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
            tempArray.add(Math.abs(Double.parseDouble(answers.get(8))-Double.parseDouble(database.get(i).get(13)))/5);
            if (answers.get(9).contains("Yes")) {
                tempArray.add(Math.abs((10/Double.parseDouble(database.get(i).get(14)))-0.127));
            }
            else {
                tempArray.add(0.0);
            }
            tempArray.add(Math.abs(Double.parseDouble(answers.get(10))-Double.parseDouble(database.get(i).get(16)))/5);
            if (answers.get(11).contains("Medicinally")) {
                tempArray.add(Math.abs((Double.parseDouble(database.get(i).get(18))/2)-0.5));
            }
            else if (answers.get(11).contains("Recreationally")) {
                tempArray.add(Math.abs((Double.parseDouble(database.get(i).get(17))/2)-0.5));
            }
            else {
                tempArray.add(0.0);
            }
            if (answers.get(12).contains("null")) {
                tempArray.add(0.0);
            }
            else {
                tempArray.add(Math.abs(Double.parseDouble(answers.get(12))-Double.parseDouble(database.get(i).get(15)))/100);
            }
            if (answers.get(13).contains("null")) {
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
