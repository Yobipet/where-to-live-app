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
import java.util.Scanner;

public class Manager {
    // Attributes
    private ArrayList<ArrayList<Double>> tierList = new ArrayList<>();
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
    }
    public void formatUserAnswers() {
        for (int i = 0; i < 100; i++) {
            ArrayList<Double> tempArray = new ArrayList<>();
            tempArray.add(Math.abs((Double.parseDouble(answers.get(0)) - Double.parseDouble(database.get(i).get(2))))/Double.parseDouble(answers.get(0)));
            tempArray.add(Math.abs((Double.parseDouble(answers.get(1)) - Double.parseDouble(database.get(i).get(3))))/Double.parseDouble(answers.get(1)));
            tempArray.add(Math.abs((Double.parseDouble(answers.get(2)) - Double.parseDouble(database.get(i).get(4))))/Double.parseDouble(answers.get(2)));
            tempArray.add(Math.abs((Double.parseDouble(answers.get(3)) - Double.parseDouble(database.get(i).get(5))))/Double.parseDouble(answers.get(3)));
            System.out.println(database.get(i).get(0) + ", " + database.get(i).get(1) + " " + tempArray);
        }
    }
}
