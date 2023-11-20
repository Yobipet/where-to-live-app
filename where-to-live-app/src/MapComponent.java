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
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
//mostly incomplete, still working out how to read excel file that stores coordinates
public class MapComponent {
    private Path2D[] paths = new Path2D[150];
    private Color[] colors = new Color[150];
    private Path2D[] pathsCounty = new Path2D[150];
    private Color[] colorsCounty = new Color[150];
    private double[] coordinates = new double[2];
    private int listLength;
    public MapComponent(boolean all) {
        if (all){
            createPathsAndColors();
        } else{
            createCountyPathsAndColors();
        }
    }
    public void createPathsAndColors(){
        for (int i = 0; i < 50; i++){
            paths[i] = new Path2D.Double();
            createPath(paths[i]);
            colors[i] = chooseColor();
        }
    }
    public void createCountyPathsAndColors(){
        for (int i = 0; i < 150; i++){
            pathsCounty[i] = new Path2D.Double();
            createCountyPath(pathsCounty[i]);
            colorsCounty[i] = chooseColor();
        }
    }
    public void createCountyPath(Path2D path){
        int[] coordinates = new int[2];
        int segmentNum = 0;
        for (int i = 0; i < getClosedPaths(); i++){
            segmentNum = 0;
            setCountyPath(segmentNum);
            path.moveTo(coordinates[0],coordinates[1]);
            segmentNum++;
            while (segmentNum < listLength) {
                setCountyPath(segmentNum);
                path.lineTo(coordinates[0], coordinates[1]);
                segmentNum++;
            }
            path.closePath();
        }
    }
    public void createPath(Path2D path){
        int[] coordinates = new int[2];
        int segmentNum = 0;
        for (int i = 0; i < getClosedPaths(); i++){
            segmentNum = 0;
            setPath(segmentNum);
            path.moveTo(coordinates[0],coordinates[1]);
            segmentNum++;
            while (segmentNum < listLength) {
                setPath(segmentNum);
                path.lineTo(coordinates[0], coordinates[1]);
                segmentNum++;
            }
            path.closePath();
        }
    }
    //the method setPath should create a coordinates list for the createPath method to read from
    //make it automatically convert to positive values in correct orientation/order for createPath()
    public void setPath(int segment){
        String[] listOne = getStatePath();
        listLength = listOne.length;
        String[] tempList = listOne[segment].split(",");
        double tempX = Double.parseDouble(tempList[0]);
        double tempY = Double.parseDouble(tempList[1]);
        double finalX = (250 - Math.abs(tempX)) * (800/250);
        double finalY = (70 - tempY) * (400/70);
        coordinates[0] = finalX;
        coordinates[1] = finalY;
    }
    public void setCountyPath(int segment){
        String[] listOne = getCountyPath();
        listLength = listOne.length;
        String[] tempList = listOne[segment].split(",");
        double tempX = Double.parseDouble(tempList[0]);
        double tempY = Double.parseDouble(tempList[1]);
        double finalX = (xMax - Math.abs(tempX)) * (800/xMax);
        double finalY = (yMax - tempY) * (400/yMax);
        coordinates[0] = finalX;
        coordinates[1] = finalY;
    }
    //the method getStatePath should simply read a single cell from the excel file, which is then read and copied to a string
    //at which point the string is split into a string list and returned
    public String[] getStatePath() {
        String row = "";
        String[] splitLine;
        try {
            FileReader fr = new FileReader("C:/Users/henry/Desktop/CoordinateData.xlsx");
            BufferedReader br = new BufferedReader(fr);
            row = br.readLine();
            splitLine = row.split(" ");
            return splitLine;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String[] getCountyPath() {
        String row = "";
        String[] splitLine;
        try {
            FileReader fr = new FileReader("C:/Users/henry/Desktop/CoordinateData.xlsx");
            BufferedReader br = new BufferedReader(fr);
            row = br.readLine();
            splitLine = row.split(" ");
            String[] completeLine = row.split(" ");
            int pathLength = completeLine.length;
            for (int i = 0; i < pathLength; i++){
                String[] tempLine = completeLine[i].split(",");
                double tempX = Double.parseDouble(tempLine[0]);
                double tempY = Double.parseDouble(tempLine[1]);
                if (tempX < minX){
                    minX = tempX;
                }
                if (tempX > maxX){
                    maxX = tempX;
                }
                if (tempY < minY){
                    minY = tempY;
                }
                if (tempY > maxY){
                    maxY = tempY;
                }
            return splitLine;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //the method getClosedPaths should check if there is additional coordinates for a new closed path in the next cell
    //and return the number of closed paths, for instance, if there are 3 cells with data, it will return 3 closed paths
    public int getClosedPaths(){
        String row = "";
        int closedPaths = 0;
        try {
            FileReader fr = new FileReader("C:/Users/henry/Desktop/CoordinateData.xlsx");
            BufferedReader br = new BufferedReader(fr);
            row = br.readLine();
            while (row != null){
                br.readLine();
                closedPaths++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return closedPaths;
    }
    //chooseColor should set color depending on corresponding state/county data from database
    public Color chooseColor(){
        Color tempColor = Color.RED; //temporary set color to red
        return tempColor;
    }
}
