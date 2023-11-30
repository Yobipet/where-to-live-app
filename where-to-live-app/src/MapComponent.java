import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.geom.Path2D;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
//mostly incomplete, still working out how to read excel file that stores coordinates
public class MapComponent {
    private Path2D path = new Path2D.Double();
    private Color color;
    private Path2D[] pathsCounty = new Path2D[150];
    private Color[] colorsCounty = new Color[150];
    private ArrayList<ArrayList<ArrayList<Double>>> stateCoords = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<Double>>> countyCoords = new ArrayList<>();
    private boolean hasIslands = false;
    private int stateNum;
    private double xMax;
    private double xMin;
    private double yMax;
    private double yMin;
    public MapComponent(boolean stateView, int stateNum) {
        xMax = 0.0;
        xMin = 0.0;
        yMax = 0.0;
        yMin = 0.0;
        this.stateNum = stateNum;
        getStatePath(stateNum);
//        getCountyPath(stateNum);
//        if (!stateView){
            createPathsAndColors();
//        } else {
//            createCountyPathsAndColors();
//        }
    }
    public void createPathsAndColors(){
            createPath(path);
//            color = chooseColor();
    }
//    public void createCountyPathsAndColors(){
//        for (int i = 0; i < 150; i++){
//            pathsCounty[i] = new Path2D.Double();
//            createCountyPath(pathsCounty[i],0);
//            createCountyPath(pathsCounty[i],1);
//            colorsCounty[i] = chooseColor();
//        }
//    }
//    public void createCountyPath(Path2D path, int countyNum){
//        for (int i = 0; i < getClosedPaths(); i++){
//            double finalX = (xMax - Math.abs(countyCoords.get(countyNum).get(0).get(0)) * (800/xMax));
//            double finalY = (yMax - countyCoords.get(countyNum).get(0).get(1)) * (400/yMax);
//            path.moveTo(finalX,finalY);
//            for (int j = 1; j < countyCoords.get(countyNum).size(); j++) {
//                finalX = (xMax - Math.abs(countyCoords.get(countyNum).get(j).get(0)) * (800/xMax));
//                finalY = (yMax - countyCoords.get(countyNum).get(0).get(1)) * (400/yMax);
//                path.lineTo(finalX,finalY);
//            }
//            path.closePath();
//        }
//    }
    public void createPath(Path2D path){
//        for (int i = 0; i < getClosedPaths(); i++){
            System.out.println("Began drawing state path.");
            if (stateNum != 49 && stateNum != 50) {
                for (int i = 0; i < stateCoords.size(); i++) {
                    double finalX = (128 - Math.abs(stateCoords.get(i).get(0).get(0))) * (800 / 60);
                    double finalY = (50 - stateCoords.get(i).get(0).get(1)) * (400 / 25);
                    path.moveTo(finalX, finalY);
                    for (int j = 1; j < stateCoords.get(i).size(); j++) {
                        finalX = (128 - Math.abs(stateCoords.get(i).get(j).get(0))) * (800 / 60);
                        finalY = (50 - stateCoords.get(i).get(j).get(1)) * (400 / 25);
                        path.lineTo(finalX, finalY);
                    }
                }
            }
            else if (stateNum == 49){
                for (int i = 0; i < stateCoords.size(); i++) {
                    System.out.println(stateNum + ": " + stateCoords);
                    double finalX = (250 - Math.abs(stateCoords.get(i).get(0).get(0))) * (800 / 200);
                    double finalY = (80 -stateCoords.get(i).get(0).get(1)) * (400 / 60);
                    path.moveTo(finalX, finalY);
                    for (int j = 1; j < stateCoords.get(i).size(); j++) {
                        finalX = (250 - Math.abs(stateCoords.get(i).get(j).get(0))) * (800 / 200);
                        finalY = (80 - stateCoords.get(i).get(j).get(1)) * (400 / 60);
                        path.lineTo(finalX, finalY);
                    }
                }
            }
            else {
                for (int i = 0; i < stateCoords.size(); i++) {
                    System.out.println(stateNum + ": " + stateCoords);
                    double finalX = (160 - Math.abs(stateCoords.get(i).get(0).get(0))) * (800 / 25);
                    double finalY = (24 -stateCoords.get(i).get(0).get(1)) * (400 / 12);
                    path.moveTo(finalX, finalY);
                    for (int j = 1; j < stateCoords.get(i).size(); j++) {
                        finalX = (160 - Math.abs(stateCoords.get(i).get(j).get(0))) * (800 / 25);
                        finalY = (24 - stateCoords.get(i).get(j).get(1)) * (400 / 12);
                        path.lineTo(finalX, finalY);
                    }
                }
            }
            System.out.println("Finished drawing state path.");
            path.closePath();
//        }
    }
    //the method setPath should create a coordinates list for the createPath method to read from
    //make it automatically convert to positive values in correct orientation/order for createPath()
//    public void setPath(int segment){
//        listLength = stateCoords.size();
//        String[] tempList = listOne[segment].split(",");
//        double tempX = Double.parseDouble(tempList[0]);
//        double tempY = Double.parseDouble(tempList[1]);
//        double finalX = (250 - Math.abs(tempX)) * (800/250);
//        double finalY = (70 - tempY) * (400/70);
//        coordinates[0] = finalX;
//        coordinates[1] = finalY;
//    }
//    public void setCountyPath(int segment){
//        String[] listOne = getCountyPath();
//        listLength = listOne.length;
//        String[] tempList = listOne[segment].split(",");
//        double tempX = Double.parseDouble(tempList[0]);
//        double tempY = Double.parseDouble(tempList[1]);
//        double finalX = (250 - Math.abs(tempX)) * (800/250);
//        double finalY = (70 - tempY) * (400/70);
//        coordinates[0] = finalX;
//        coordinates[1] = finalY;
//    }
    //the method getStatePath should simply read a single cell from the excel file, which is then read and copied to a string
    //at which point the string is split into a string list and returned
    public void getStatePath(int stateNum) {
        String row = "";
        try {
            FileReader fr = new FileReader("StateCoordinates.csv");
            BufferedReader br = new BufferedReader(fr);
            for (int i = 0; i < stateNum; i++) {
                row = br.readLine();
            }
            String[] landMasses = row.split(",");
            if (landMasses.length > 1) {
                hasIslands = true;
            }
            for (int k = 0; k < landMasses.length; k++) {
                stateCoords.add(new ArrayList<>());
                String[] cols = landMasses[k].split(" ");
                for (int j = 0; j < cols.length; j++) {
                    stateCoords.get(k).add(new ArrayList<>());
                    String[] pair = cols[j].split(":");
                    stateCoords.get(k).get(j).add(Double.parseDouble(pair[0]));
                    stateCoords.get(k).get(j).add(Double.parseDouble(pair[1]));
                    if (stateCoords.get(k).get(j).get(0) > xMax) {
                        xMax = stateCoords.get(k).get(j).get(0);
                    }
                    if (stateCoords.get(k).get(j).get(0) < xMin) {
                        xMin = stateCoords.get(k).get(j).get(0);
                    }
                    if (stateCoords.get(k).get(j).get(1) > yMax) {
                        yMax = stateCoords.get(k).get(j).get(1);
                    }
                    if (stateCoords.get(k).get(j).get(1) < yMin) {
                        yMin = stateCoords.get(k).get(j).get(1);
                    }
                }
            }
            System.out.println(stateCoords);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public void getCountyPath(int stateNum) {
//        String row1 = "";
//        String row2 = "";
//        try {
//            FileReader fr = new FileReader("CountyCoordinates.csv");
//            BufferedReader br = new BufferedReader(fr);
//            br.readLine();
//            for (int i = 0; i < stateNum; i++) {
//                row1 = br.readLine();
//                row2 = br.readLine();
//            }
//            String[] cols1 = row1.split(" ");
//            String[] cols2 = row2.split(" ");
//            countyCoords.add(new ArrayList<>());
//            for (int j = 0; j < cols1.length; j++) {
//                countyCoords.get(0).add(new ArrayList<>());
//                String[] pair = cols1[j].split(",");
//                countyCoords.get(0).get(j).add(Double.parseDouble(pair[0]));
//                countyCoords.get(0).get(j).add(Double.parseDouble(pair[1]));
//            }
//            countyCoords.add(new ArrayList<>());
//            for (int k = 0; k < cols1.length; k++) {
//                countyCoords.get(1).add(new ArrayList<>());
//                String[] pair = cols2[k].split(",");
//                countyCoords.get(1).get(k).add(Double.parseDouble(pair[0]));
//                countyCoords.get(1).get(k).add(Double.parseDouble(pair[1]));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    //the method getClosedPaths should check if there is additional coordinates for a new closed path in the next cell
    //and return the number of closed paths, for instance, if there are 3 cells with data, it will return 3 closed paths
//    public int getClosedPaths(){
//        String row = "";
//        int closedPaths = 0;
//        try {
//            FileReader fr = new FileReader("StateCoordinates.xlsx");
//            BufferedReader br = new BufferedReader(fr);
//            row = br.readLine();
//            while (row != null){
//                br.readLine();
//                closedPaths++;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return closedPaths;
//    }
    //chooseColor should set color depending on corresponding state/county data from database
    public Color chooseColor(){
        Color tempColor = Color.RED; //temporary set color to red
        return tempColor;
    }

    // SETTERS AND GETTERS
    public Path2D getPaths() {
        return this.path;
    }
    public int getStateNum() {
        return this.stateNum;
    }
}
