/*
Class: MapData
Author: Samdine Murray, Dylan Potter, Henry Sanders, Nole Liu
Created: 10/6/2023
Last Modified: 10/6/2023

Purpose: Contains all the map information read from the Manager.

Attributes: -dataBase: ArrayList<>()
            -dataFile: File

Methods: +readDataFromFile(): void
 */

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;

public class MapData {
    private ArrayList<Double> dataBase = new ArrayList<>(); // Set it to Double to placehold
    private File dataFile = new File("DataBase.csv"); // Placeholder name

    public void readDataFromFile() {

    }

    // Getters and Setters
    public void setDataBase(ArrayList<Double> dataBase) {
        this.dataBase = dataBase;
    }
    public ArrayList<Double> getDataBase() {
        return dataBase;
    }
    public void setDataFile(File dataFile) {
        this.dataFile = dataFile;
    }
    public File getDataFile() {
        return dataFile;
    }
}
