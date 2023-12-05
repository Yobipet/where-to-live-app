import java.awt.Graphics;
import java.awt.geom.AffineTransform;
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
    private ArrayList<Path2D> countyPaths = new ArrayList<>();
    private Color color;
    private Path2D[] pathsCounty = new Path2D[150];
    private Color[] colorsCounty = new Color[150];
    private ArrayList<ArrayList<ArrayList<Double>>> stateCoords = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> countyCoords = new ArrayList<>();
    private boolean stateView;
    private int stateNum;
    private double xMax;
    private double xMin;
    private double yMax;
    private double yMin;
    public MapComponent(boolean sV, int stateNum) {
        this.xMax = 0.0;
        this.xMin = 0.0;
        this.yMax = 0.0;
        this.yMin = 0.0;
        this.stateView = sV;
        this.stateNum = stateNum;
        getStatePath(stateNum);
        getCountyPath(stateNum);
        if (!stateView){
            createPath(path);
        } else {
            createCountyPaths(countyPaths);
        }
    }
    public void createPath(Path2D path){
//        for (int i = 0; i < getClosedPaths(); i++){
        // Mainland US
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
        // Alaska
        else if (stateNum == 49){
            for (int i = 0; i < stateCoords.size(); i++) {
                double finalX = ((Math.abs(xMax) + 1) - Math.abs(stateCoords.get(i).get(0).get(0))) * (800 / 200);
                double finalY = ((yMax + 3) - stateCoords.get(i).get(0).get(1)) * (400 / 50);
                path.moveTo(finalX, finalY);
                for (int j = 1; j < stateCoords.get(i).size(); j++) {
                    finalX = ((Math.abs(xMax) + 1) - Math.abs(stateCoords.get(i).get(j).get(0))) * (800/200);
                    finalY = ((yMax + 3) - stateCoords.get(i).get(j).get(1)) * (400 / 50);
                    path.lineTo(finalX, finalY);
                }
            }
        }
        // Hawaii
        else {
            for (int i = 0; i < stateCoords.size(); i++) {
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
        path.closePath();
//        }
    }
    public void createCountyPaths(ArrayList<Path2D> countyPaths){
        System.out.println("xMax: " + xMax);
        System.out.println("yMax: " + yMax);
        if (stateNum != 49 && stateNum != 50) {
            for (int k = 0; k < 2; k++) {
                Path2D tempPath = new Path2D.Double();
                for (int i = 0; i < countyCoords.get(k).size(); i++) {
                    double finalX = ((Math.abs(xMax) + 1) - Math.abs(countyCoords.get(k).get(i).get(0).get(0))) * (400 / Math.abs(xMax)) * 10;
                    double finalY = ((yMax + 1) - countyCoords.get(k).get(i).get(0).get(1)) * (400 / yMax) * 5;
                    tempPath.moveTo(finalX, finalY);
                    for (int j = 1; j < countyCoords.get(k).get(i).size(); j++) {
                        finalX = ((Math.abs(xMax) + 1) - Math.abs(countyCoords.get(k).get(i).get(j).get(0))) * (400 / Math.abs(xMax)) * 10;
                        finalY = ((yMax + 1) - countyCoords.get(k).get(i).get(j).get(1)) * (400 / yMax) * 5;
                        tempPath.lineTo(finalX, finalY);
                    }
                }
                tempPath.closePath();
                countyPaths.add(tempPath);
            }
            for (int i = 0; i < stateCoords.size(); i++) {
                double finalX = ((Math.abs(xMax) + 1) - Math.abs(stateCoords.get(i).get(0).get(0))) * ((400 / Math.abs(xMax))) * 10;
                double finalY = ((yMax + 1) - stateCoords.get(i).get(0).get(1)) * (400 / yMax) * 5;
                path.moveTo(finalX, finalY);
                for (int j = 1; j < stateCoords.get(i).size(); j++) {
                    finalX = ((Math.abs(xMax) + 1) - Math.abs(stateCoords.get(i).get(j).get(0))) * ((400 / Math.abs(xMax))) * 10;
                    finalY = ((yMax + 1) - stateCoords.get(i).get(j).get(1)) * (400 / yMax) * 5;
                    path.lineTo(finalX, finalY);
                }
            }
            path.closePath();
        }
        else if (stateNum == 50) {
            for (int k = 0; k < 2; k++) {
                Path2D tempPath = new Path2D.Double();
                for (int i = 0; i < countyCoords.get(k).size(); i++) {
                    double finalX = ((Math.abs(xMax) + 1) - Math.abs(countyCoords.get(k).get(i).get(0).get(0))) * 50;
                    double finalY = ((yMax + 1) - countyCoords.get(k).get(i).get(0).get(1)) * 50;
                    tempPath.moveTo(finalX, finalY);
                    for (int j = 1; j < countyCoords.get(k).get(i).size(); j++) {
                        finalX = ((Math.abs(xMax) + 1) - Math.abs(countyCoords.get(k).get(i).get(j).get(0))) * 50;
                        finalY = ((yMax + 1) - countyCoords.get(k).get(i).get(j).get(1)) * 50;
                        tempPath.lineTo(finalX, finalY);
                    }
                }
                tempPath.closePath();
                countyPaths.add(tempPath);
            }
            for (int i = 0; i < stateCoords.size(); i++) {
                double finalX = ((Math.abs(xMax) + 1) - Math.abs(stateCoords.get(i).get(0).get(0))) * 50;
                double finalY = ((yMax + 1) - stateCoords.get(i).get(0).get(1)) * 50;
                path.moveTo(finalX, finalY);
                for (int j = 1; j < stateCoords.get(i).size(); j++) {
                    finalX = ((Math.abs(xMax) + 1) - Math.abs(stateCoords.get(i).get(j).get(0))) * 50;
                    finalY = ((yMax + 1) - stateCoords.get(i).get(j).get(1)) * 50;
                    path.lineTo(finalX, finalY);
                }
            }
            System.out.println("Hawaii coords: " + path.getBounds().getLocation().x + "," + path.getBounds().getLocation().y);
            path.closePath();
        }
        else {
            for (int k = 0; k < 2; k++) {
                Path2D tempPath = new Path2D.Double();
                for (int i = 0; i < countyCoords.get(k).size(); i++) {
                    double finalX = (179 - Math.abs(countyCoords.get(k).get(i).get(0).get(0))) * (400 / Math.abs(xMax)) * 10;
                    double finalY = (30 - countyCoords.get(k).get(i).get(0).get(1)) * (400 / yMax) * 5;
                    tempPath.moveTo(finalX, finalY);
                    for (int j = 1; j < countyCoords.get(k).get(i).size(); j++) {
                        finalX = (179 - Math.abs(countyCoords.get(k).get(i).get(j).get(0))) * (400 / Math.abs(xMax)) * 10;
                        finalY = (30 - countyCoords.get(k).get(i).get(j).get(1)) * (400 / yMax) * 5;
                        tempPath.lineTo(finalX, finalY);
                    }
                }
                tempPath.closePath();
                AffineTransform at = new AffineTransform();
                at.scale(-1.3,-1.3);
                tempPath.transform(at);
                AffineTransform at2 = new AffineTransform();
                at2.rotate(Math.toRadians(90));
                at2.translate(720,-30);
                tempPath.transform(at2);
                countyPaths.add(tempPath);
            }
            for (int i = 0; i < stateCoords.size(); i++) {
                double finalX = ((Math.abs(xMax) + 1) - Math.abs(stateCoords.get(i).get(0).get(0))) * ((400 / Math.abs(xMax))) * 10;
                double finalY = ((yMax + 1) - stateCoords.get(i).get(0).get(1)) * (400 / yMax) * 5;
                path.moveTo(finalX, finalY);
                for (int j = 1; j < stateCoords.get(i).size(); j++) {
                    finalX = ((Math.abs(xMax) + 1) - Math.abs(stateCoords.get(i).get(j).get(0))) * ((400 / Math.abs(xMax))) * 10;
                    finalY = ((yMax + 1) - stateCoords.get(i).get(j).get(1)) * (400 / yMax) * 5;
                    path.lineTo(finalX, finalY);
                }
            }
            path.closePath();
            AffineTransform at = new AffineTransform();
            at.scale(-0.78,0.78);
            at.translate(-800,0);
            path.transform(at);
        }
    }
    public void getStatePath(int stateNum) {
        String row = "";
        try {
            FileReader fr = new FileReader("StateCoordinates.csv");
            BufferedReader br = new BufferedReader(fr);
            for (int i = 0; i < stateNum; i++) {
                row = br.readLine();
            }
            String[] landMasses = row.split(",");
            for (int k = 0; k < landMasses.length; k++) {
                stateCoords.add(new ArrayList<>());
                String[] cols = landMasses[k].split(" ");
                for (int j = 0; j < cols.length; j++) {
                    stateCoords.get(k).add(new ArrayList<>());
                    String[] pair = cols[j].split(":");
                    stateCoords.get(k).get(j).add(Double.parseDouble(pair[0]));
                    stateCoords.get(k).get(j).add(Double.parseDouble(pair[1]));
                    if (Math.abs(stateCoords.get(k).get(j).get(0)) > Math.abs(xMax)) {
                        xMax = stateCoords.get(k).get(j).get(0);
                    }
                    else if (Math.abs(stateCoords.get(k).get(j).get(0)) < Math.abs(xMin)) {
                        xMin = stateCoords.get(k).get(j).get(0);
                    }
                    if (stateCoords.get(k).get(j).get(1) > yMax) {
                        yMax = stateCoords.get(k).get(j).get(1);
                    }
                    else if (stateCoords.get(k).get(j).get(1) < yMin) {
                        yMin = stateCoords.get(k).get(j).get(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getCountyPath(int stateNum) {
        String row1 = "";
        String row2 = "";
        try {
            FileReader fr = new FileReader("CountyCoordinates.csv");
            BufferedReader br = new BufferedReader(fr);
            for (int i = 0; i < stateNum; i++) {
                row1 = br.readLine();
                row2 = br.readLine();
            }
            String[] landMasses1 = row1.split(",");
            String[] landMasses2 = row2.split(",");
            countyCoords.add(new ArrayList<>());
            for (int k = 0; k < landMasses1.length; k++) {
                String[] cols = landMasses1[k].split(" ");
                countyCoords.get(0).add(new ArrayList<>());
                for (int j = 0; j < cols.length; j++) {
                    countyCoords.get(0).get(k).add(new ArrayList<>());
                    String[] pair = cols[j].split(":");
                    countyCoords.get(0).get(k).get(j).add(Double.parseDouble(pair[0]));
                    countyCoords.get(0).get(k).get(j).add(Double.parseDouble(pair[1]));
                }
            }
            countyCoords.add(new ArrayList<>());
            for (int m = 0; m < landMasses2.length; m++) {
                String[] cols = landMasses2[m].split(" ");
                countyCoords.get(1).add(new ArrayList<>());
                for (int l = 0; l < cols.length; l++) {
                    countyCoords.get(1).get(m).add(new ArrayList<>());
                    String[] pair = cols[l].split(":");
                    countyCoords.get(1).get(m).get(l).add(Double.parseDouble(pair[0]));
                    countyCoords.get(1).get(m).get(l).add(Double.parseDouble(pair[1]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SETTERS AND GETTERS
    public Path2D getPaths() {
        return this.path;
    }
    public ArrayList<Path2D> getCountyPaths() {
        return this.countyPaths;
    }
    public int getStateNum() {
        return this.stateNum;
    }
    public double getxMax() {
        return Math.abs(xMax);
    }
    public double getyMax() {
        return Math.abs(yMax);
    }
}
