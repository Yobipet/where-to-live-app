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
//    private JLayeredPane mainPanel;
    private JPanel mainPanel;
    private static JComboBox dropDown1;
    private static JComboBox dropDown2;
    //private BufferedImage image;
//    private Color fillColor;
    private ArrayList<Path2D> path = new ArrayList<>();
//    private int pathLength;
//    private double[] coordinates = new double[2];
//    private File statePath = new File("C:/Users/henry/Desktop/FloridaPath.txt");
//    private int segmentNum;
    private ArrayList<MapComponent> mapPieces = new ArrayList<>();
    private ArrayList<Integer> tierList;
    private boolean mainlandDrawn = false;
    private boolean backFlag = false;
    public Map(ArrayList<Integer> tierList) {
        this.tierList = tierList;
        ArrayList<Integer> stateTierList = new ArrayList<>();
        for (int j = 0; j < tierList.size(); j+=2) {
            int avgTierValue = (int) Math.round((tierList.get(j) + tierList.get(j+1))/2);
            stateTierList.add(avgTierValue);
        }
        // LOADS IN ALL MAPCOMPONENTS WITH PROPER INFORMATION
        for (int i = 1; i <= 50; i++) {
            mapPieces.add(new MapComponent(false, i));
            System.out.println("Loading state " + (i) + ".");
            path.add(mapPieces.get(i-1).getPaths());
            System.out.println("State " + i + " loaded.");
        }
        System.out.println("Loaded map path from component instance.");

        // INITIALIZES MAIN WINDOW
//        mapFrame = new JFrame("Map Frame");
//        mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        mapFrame.setSize(1600,800);
//        mainPanel = new JLayeredPane();
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        GridBagConstraints gbc = new GridBagConstraints();

        // INITIALIZES THE HAWAII AND ALASKA PANELS
        JPanel alaskaPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D map = (Graphics2D) g;
                map.setColor(Color.RED);
                map.fill(path.get(48));
                System.out.println("Colored in state 49");
                map.setColor(Color.BLACK);
                map.draw(path.get(48));
                System.out.println("Outlined state 49");
                map.drawRect(0, 0, 199, 199);
                if (path.get(48) != null) {
                    System.out.println("ACTUALLY PRINTED ALASKA");
                }
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 200);
            }
        };
        gbc.ipady = 0;
        gbc.gridx = 7;
        gbc.gridy = 1;
        mainPanel.add(alaskaPanel, gbc);
        JPanel hawaiiPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D map = (Graphics2D) g;
                map.setColor(Color.RED);
                map.fill(path.get(49));
                System.out.println("Colored in state 50");
                map.setColor(Color.BLACK);
                map.draw(path.get(49));
                System.out.println("Outlined state 50");
                map.drawRect(0, 0, 199, 199);
                if (path.get(49) != null) {
                    System.out.println("ACTUALLY PRINTED HAWAII");
                }
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 400);
            }
        };
        gbc.gridx = 6;
        gbc.gridy = 1;
        mainPanel.add(hawaiiPanel, gbc);

        // INITIALIZES THE MAINLAND U.S. PANEL
        JPanel USAPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D map = (Graphics2D) g;
                for (int i = 0; i < 48; i++) {
                    map.setColor(tierListColor(stateTierList.get(i)));
                    map.fill(path.get(i));
                }
                for (int j = 0; j < 48; j++) {
                    map.setColor(Color.BLACK);
                    map.draw(path.get(j));
                }
                System.out.println("Colored in mainland U.S.");
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 400);
            }
        };
        gbc.gridx = 5;
        gbc.gridy = 1;
        mainPanel.add(USAPanel, gbc);
        System.out.println("Finished drawing mainland U.S.");

        // INITIALIZES LABELS
        JLabel label1 = new JLabel("Map");
        label1.setBounds(750, 100, 100, 50);
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.insets = new Insets(10,0,0,0);
        mainPanel.add(label1, gbc);

        // INITIALIZES BUTTONS
        JButton button1 = new JButton("BACK");
        button1.setBounds(1400, 700, 100, 50);
        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.gridy = 10;
        gbc.gridx = 4;
        gbc.weighty = 1.0;
        mainPanel.add(button1, gbc);
        button1.addActionListener(this::pressBack);
//        mapFrame.add(layeredPane);
//        mapFrame.pack();
//        mapFrame.setVisible(true);
//        mapFrame.validate();
        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.NORTH);
    }
    private void pressBack(ActionEvent e) {
        backFlag = true;
    }
    public Color tierListColor(int rankNum) {
        if (rankNum == 1) {
            return new Color(100,0,0);
        }
        else if (rankNum == 2) {
            return new Color(200,0,0);
        }
        else if (rankNum == 3) {
            return Color.ORANGE;
        }
        else if (rankNum == 4) {
            return Color.YELLOW;
        }
        else {
            return new Color(0,200,0);
        }
    }
//    public void mapUpdate(ItemEvent e){
//        System.out.println("The method mapUpdate() has been called.");
//        setFillColor(Color.RED); //temporary fill color
//        setPath(0);
//        repaint();
//        System.out.println("The method repaint() has been called.");
//    }
//    public void setFillColor(Color newColor){
//        this.fillColor = newColor;
//    }

//    @Override
//    protected void paintComponent(Graphics g){
//        super.paintComponent(g);
//        System.out.println("The method paintComponent() have been called.");
//        if (!mainlandDrawn) {
//            Graphics2D map = (Graphics2D) g;
////            for (int i = 0; i < mapPieces.size(); i++) {
//            for (int i = 0; i < 48; i++) {
//                map.setColor(Color.RED);
//                map.fill(path.get(i));
//                System.out.println("Colored in state " + (mapPieces.get(i).getStateNum()));
//            }
//            for (int j = 0; j < 48; j++) {
//                map.setColor(Color.BLACK);
//                map.draw(path.get(j));
//                System.out.println("Outlined state " + (mapPieces.get(j).getStateNum()));
//            }
//            System.out.println("The path should have been drawn.");
//        }
//        else {
//            Graphics2D map = (Graphics2D) g;
//            map.setColor(Color.RED);
//            map.fill(path.get(48));
//            map.fill(path.get(49));
//            map.setColor(Color.BLACK);
//            map.draw(path.get(48));
//            map.draw(path.get(49));
//        }
//    }

//    private JPanel getPaintedMapPiece(int stateNum) {
//        JPanel inside = new JPanel();
//        Graphics2D mapPiece = (Graphics2D) inside.getGraphics();
//        mapPiece.setColor(Color.BLUE);
//        mapPiece.fill(path.get(stateNum));
//        mapPiece.setColor(Color.BLACK);
//        mapPiece.draw(path.get(stateNum));
//        return inside;
//    }
//    public void setPath(int selected){
//        path = new Path2D.Double();
//        segmentNum = 0;
//        getStatePath();
//        /*
//        if(selected == 0) {
//            getUSAPath();
//        }
//        else {
//            getStatePath();
//        }
//         */
//        path.moveTo(coordinates[0],coordinates[1]);
//        segmentNum++;
//        while (segmentNum < pathLength) {
//            getStatePath();
//            path.lineTo(coordinates[0], coordinates[1]);
//            segmentNum++;
//        }
//        path.closePath();
//        System.out.println("The path has been set.");
//    }
//    public void getUSAPath() {
//        String row = "";
//        try {
//            FileReader fr = new FileReader("C:/Users/henry/Desktop/FloridaPath.txt");
//            BufferedReader br = new BufferedReader(fr);
//            for(int i = 0; i < 9; i++) {
//                row = br.readLine();
//                if (row == null) {
//                    path.closePath();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public void getStatePath() {
//        String row = "";
//        Double minX = 1000.0;
//        Double minY = 1000.0;
//        Double maxX = 0.0;
//        Double maxY = 0.0;
//        try {
//            FileReader fr = new FileReader(statePath);
//            BufferedReader br = new BufferedReader(fr);
//            row = br.readLine();
//            String[] completeLine = row.split(" ");
//            pathLength = completeLine.length;
//            for (int i = 0; i < pathLength; i++){
//                String[] tempLine = completeLine[i].split(",");
//                double tempX = Double.parseDouble(tempLine[0]);
//                double tempY = Double.parseDouble(tempLine[1]);
//                if (tempX < minX){
//                    minX = tempX;
//                }
//                if (tempX > maxX){
//                    maxX = tempX;
//                }
//                if (tempY < minY){
//                    minY = tempY;
//                }
//                if (tempY > maxY){
//                    maxY = tempY;
//                }
//            }
//            double lengthX = Math.abs(maxX - minX);
//            double lengthY = Math.abs(maxY - minY);
//            String[] tempLine = completeLine[segmentNum].split(",");
//            double scaleFactorX = 800 / lengthX;
//            double scaleFactorY = 400 / lengthY;
//            double x = (minX - Double.parseDouble(tempLine[0])) % 800;
//            double y = (maxY - Double.parseDouble(tempLine[1])) % 400;
//            coordinates[0] = Math.abs(x) * scaleFactorY;
//            coordinates[1] = Math.abs(y) * scaleFactorY;
//            if (coordinates[0] > 800) {
//                coordinates[0] = 800;
//            }
//            if (coordinates[1] > 400) {
//                coordinates[1] = 400;
//            }
//            System.out.println("Coordinates of point: " + coordinates[0] + ", " + coordinates[1]);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    // SETTERS AND GETTERS
    public boolean getBackFlag() {
        return backFlag;
    }
}
