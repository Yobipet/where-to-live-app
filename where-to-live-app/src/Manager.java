/*
Class: Manager
Author: Samdine Murray, Dylan Potter, Henry Sanders, Nole Liu
Created: 10/2/2023
Last Modified: 10/15/2023

Purpose: This class contains most of the calculation methods and is the central class that communicates
between all classes.

Attributes: -tierList: int[]
            -data: MapData()
            -regionFile: File
            -database: ArrayList<ArrayList<String>>()

Methods: +tierListAdd(): void
         +parseDatabase(): void
 */

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Scanner;

public class Manager {
    // Attributes
    private int[] tierList;
    private MapData data;
    private File regionFile = new File("RegionDatabase.csv");
    private ArrayList<ArrayList<String>> database = new ArrayList<>();


    // Methods
    public void runProgram() {
        //parseDatabase();
        //testCase();
    }
    public void testCase() {
        System.out.println("Which state would you like to examine?");
        Scanner ans = new Scanner(System.in);
        String reqState = ans.next();
        System.out.println("Would you like to know (answer with letter)\na) The Violent Crime Rate [per 100,000 people]\nb) The National Risk Index Score\nc) The Avg High Temp (F)\nd) The Avg Low Temp (F)\ne) The Avg Housing Price ($)\nf) The Housing Vacancy (%)");
        ans.nextLine();
        String reqData = ans.nextLine();
        String[] letters = {"a","b","c","d","e","f"};
        for (int i = 0;i < database.size();i++) {
            if (reqState.equalsIgnoreCase(database.get(i).get(0))) {
                for (int j = 0;j < letters.length;j++) {
                    if (reqData.equalsIgnoreCase(letters[j])) {
                        System.out.println("Here is your data.\n" + database.get(i).get(j+1));
                    }
                }
            }
        }
    }
    public void parseDatabase() {
        try {
            FileReader fr = new FileReader(regionFile);
            BufferedReader br = new BufferedReader(fr);
            // System.out.println("Began parsing database.");
            String titleRow = br.readLine();
            for (int i = 0;i < 50; i++) {
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

    public void tierListAdd() {

    }

}
