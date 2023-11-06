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


/*
Class: Map
Author: Samdine Murray, Dylan Potter, Henry Sanders, Nole Liu
Created: 10/6/2023
Last Modified: 10/6/2023

Purpose:

Attributes: -mapFrame: JFrame
            -layeredPane: JLayeredPane
            -dropDown1: JComboBox
            -dropDown2: JComboBox
            -fillColor: Color
            -path: Path2D
            -pathLength: int
            -coordinates: double[]
            -statePath: File
            -segmentNum: int

Methods: +pressBack(): void
         +selectMapMode(): void
         +selectState(): void
         +mapUpdate(): void
         +setFillColor(): void
         +selectState(): void
         +paintComponent(): void
         +setPath(): void
         +getStatePath(): void
 */
public class Map extends JPanel {
    private JFrame mapFrame;
    private JLayeredPane layeredPane;
    static JComboBox dropDown1;
    static JComboBox dropDown2;
    //private BufferedImage image;
    private Color fillColor;
    private Path2D path;
    private int pathLength;
    private double[] coordinates = new double[2];
    private File statePath = new File("C:/Users/henry/Desktop/FloridaPath.txt");
    private int segmentNum;
    Map() {
        mapFrame = new JFrame("Map Frame");
        mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mapFrame.setSize(1600,800);
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1600, 800));
        JPanel pathPanel = new JPanel();
        pathPanel.setBounds(400, 200, 800, 400);
        pathPanel.setPreferredSize(new Dimension(300, 300));
        this.setPreferredSize(new Dimension(300, 300));
        pathPanel.setLayout(new BorderLayout());
        pathPanel.add(this, BorderLayout.CENTER);
        pathPanel.setBackground(Color.WHITE);
        layeredPane.add(pathPanel, JLayeredPane.DEFAULT_LAYER);
        JLabel label1 = new JLabel("Map");
        label1.setBounds(750, 100, 100, 50);
        layeredPane.add(label1, JLayeredPane.PALETTE_LAYER);
        JLabel label2 = new JLabel("Mapmode:");
        label2.setBounds(625, 675, 75, 25);
        layeredPane.add(label2, JLayeredPane.PALETTE_LAYER);
        JLabel label3 = new JLabel("State:");
        label3.setBounds(625, 725, 75, 25);
        layeredPane.add(label3, JLayeredPane.PALETTE_LAYER);
        JButton button1 = new JButton("BACK");
        button1.setBounds(1400, 700, 100, 50);
        layeredPane.add(button1, JLayeredPane.PALETTE_LAYER);
        button1.addActionListener(this::pressBack);
        selectMapMode();
        selectState();
        mapFrame.add(layeredPane);
        mapFrame.setVisible(true);
        mapFrame.validate();
    }
    private void pressBack(ActionEvent e) {
        mapFrame.hide();
    }
    public void selectMapMode() {
        String temp = "disasters, crime, density, temperature, vacancy, tax";
        String[] line = temp.split(",");
        String choices[] = {line[1], line[2], line[3], line[4], line[5]};
        dropDown1 = new JComboBox(choices);
        dropDown1.setBounds(700, 675, 200, 25);
        layeredPane.add(dropDown1, JLayeredPane.PALETTE_LAYER);
        dropDown1.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getSource() == Map.dropDown1) {
                    dropDown1.removeItemListener(dropDown1.getItemListeners()[0]);
                    //set return value that can be used to get data from database
                    // should call a method to set fill color
                    dropDown1.addItemListener(dropDown1.getItemListeners()[0]);
                }
            }
        });
    }
    public void selectState() {
        String temp = "USA, florid, flori, flor, flo, fl";
        String[] line = temp.split(",");
        String choices[] = {line[0], line[1], line[2], line[3], line[4]};
        dropDown2 = new JComboBox(choices);
        dropDown2.setBounds(700, 725, 200, 25);
        layeredPane.add(dropDown2, JLayeredPane.PALETTE_LAYER);
        dropDown2.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getSource() == Map.dropDown2) {
                    dropDown2.removeItemListener(dropDown2.getItemListeners()[0]);
                    mapUpdate(e);
                    dropDown2.addItemListener(dropDown2.getItemListeners()[0]);
                }
            }
        });
        System.out.println("The method selectState() has been called.");
    }
    public void mapUpdate(ItemEvent e){
        System.out.println("The method mapUpdate() has been called.");
        setFillColor(Color.RED); //temporary fill color
        setPath(0);
        repaint();
        System.out.println("The method repaint() has been called.");
    }
    public void setFillColor(Color newColor){
        this.fillColor = newColor;
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        System.out.println("The method paintComponent() have been called.");
        if (path != null) {
            Graphics2D map = (Graphics2D) g;
            map.setColor(fillColor);
            map.fill(path);
            map.setColor(Color.BLACK);
            map.draw(path);
            System.out.println("The path should have been drawn.");
        }
    }
    public void setPath(int selected){
        path = new Path2D.Double();
        segmentNum = 0;
        getStatePath();
        /*
        if(selected == 0) {
            getUSAPath();
        }
        else {
            getStatePath();
        }
         */
        path.moveTo(coordinates[0],coordinates[1]);
        segmentNum++;
        while (segmentNum < pathLength) {
            getStatePath();
            path.lineTo(coordinates[0], coordinates[1]);
            segmentNum++;
        }
        path.closePath();
        System.out.println("The path has been set.");
    }
    public void getUSAPath() {
        String row = "";
        try {
            FileReader fr = new FileReader("C:/Users/henry/Desktop/FloridaPath.txt");
            BufferedReader br = new BufferedReader(fr);
            for(int i = 0; i < 9; i++) {
                row = br.readLine();
                if (row == null) {
                    path.closePath();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getStatePath() {
        String row = "";
        Double minX = 1000.0;
        Double minY = 1000.0;
        Double maxX = 0.0;
        Double maxY = 0.0;
        try {
            FileReader fr = new FileReader(statePath);
            BufferedReader br = new BufferedReader(fr);
            row = br.readLine();
            String[] completeLine = row.split(" ");
            pathLength = completeLine.length;
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
            }
            double lengthX = Math.abs(maxX - minX);
            double lengthY = Math.abs(maxY - minY);
            String[] tempLine = completeLine[segmentNum].split(",");
            double scaleFactorX = 800 / lengthX;
            double scaleFactorY = 400 / lengthY;
            double x = (minX - Double.parseDouble(tempLine[0])) % 800;
            double y = (maxY - Double.parseDouble(tempLine[1])) % 400;
            coordinates[0] = Math.abs(x) * scaleFactorY;
            coordinates[1] = Math.abs(y) * scaleFactorY;
            if (coordinates[0] > 800) {
                coordinates[0] = 800;
            }
            if (coordinates[1] > 400) {
                coordinates[1] = 400;
            }
            System.out.println("Coordinates of point: " + coordinates[0] + ", " + coordinates[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
