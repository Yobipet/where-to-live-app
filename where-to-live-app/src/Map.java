import java.awt.Graphics;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.metal.MetalButtonUI;
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
//    private static JComboBox dropDown1;
//    private static JComboBox dropDown2;
    //private BufferedImage image;
//    private Color fillColor;
    private JLabel stateSelected = new JLabel("State Selected: none    ");
    private ArrayList<String> stateNames = new ArrayList<>();
    private ArrayList<Path2D> path = new ArrayList<>();
//    private int pathLength;
//    private double[] coordinates = new double[2];
//    private File statePath = new File("C:/Users/henry/Desktop/FloridaPath.txt");
//    private int segmentNum;
    private ArrayList<MapComponent> mapPieces = new ArrayList<>();
    private ArrayList<Integer> tierList;
    private boolean mainlandDrawn = false;
    private boolean backFlag = false;
    public Map(ArrayList<Integer> masterTierList, ArrayList<String> stateNames) {
        // INITIALIZES ARRAYLIST VALUES
        this.tierList = masterTierList;
        this.stateNames = stateNames;
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
        this.setLayout(new BorderLayout());
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        GridBagConstraints gbc = new GridBagConstraints();
        stateSelected.setFont(new Font("Serif",Font.PLAIN,18));

        // INITIALIZES THE HAWAII AND ALASKA PANELS
        JPanel otherStatesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel alaskaPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D map = (Graphics2D) g;
                map.setColor(tierListColor(stateTierList.get(48)));
                // Flips Alaska horizontally (coordinates are backwards in database)
                map.scale(-1.0,1.0);
                map.translate(-200,0);
                map.fill(path.get(48));
                map.setColor(Color.BLACK);
                map.draw(path.get(48));
                map.drawRect(0, 0, 199, 199);
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 200);
            }
        };
        JButton alaskaButton = new JButton() {
            Shape polygon;
            @Override
            public boolean contains(int x, int y) {
                if (polygon == null || !polygon.getBounds().equals(getBounds())) {
                    polygon = new Area(path.get(48));
                    AffineTransform at = new AffineTransform();
                    at.translate(200, 0);
                    at.scale(-1, 1);
                    polygon = at.createTransformedShape(polygon);
                }
                return polygon.contains(x, y);
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 200);
            }
        };
        alaskaButton.setOpaque(false);
        alaskaButton.setContentAreaFilled(false);
        alaskaButton.setBorderPainted(false);
        alaskaPanel.add(alaskaButton);
        alaskaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame tempStateWindow = new Frame();
//                    JLabel stateNum = new JLabel(Integer.toString(finalK+1));
//                    tempStateWindow.add(stateNum);
                MapComponent mc = new MapComponent(true,49);
                JPanel stateViewPanel = new JPanel(new FlowLayout()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D county = (Graphics2D) g;
                        county.setColor(Color.GRAY);
                        county.fill(mc.getPaths());
                        county.setColor(tierListColor(tierList.get(((49) * 2 - 1) - 1)));
                        System.out.println("County 1 ranking: " + (tierList.get(((49) * 2 - 1) - 1)));
                        county.fill(mc.getCountyPaths().get(0));
                        county.setColor(tierListColor(tierList.get((49) * 2 - 1)));
                        System.out.println("County 2 ranking: " + (tierList.get((49) * 2 - 1)));
                        county.fill(mc.getCountyPaths().get(1));
                        county.setColor(Color.BLACK);
                        county.draw(mc.getPaths());
                        county.draw(mc.getCountyPaths().get(0));
                        county.draw(mc.getCountyPaths().get(1));
                    }
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(700, 700);
                    }
                };
                tempStateWindow.add(stateViewPanel);
                tempStateWindow.pack();
                tempStateWindow.setVisible(true);
            }
        });
//        alaskaButton.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                stateSelected.setText("State Selected: " + stateNames.get(48) + "    ");
//            }
//
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                stateSelected.setText("State Selected: none    ");
//            }
//        });
        otherStatesPanel.add(alaskaPanel);
        JPanel hawaiiPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D map = (Graphics2D) g;
                map.setColor(tierListColor(stateTierList.get(49)));
                map.fill(path.get(49));
                map.setColor(Color.BLACK);
                map.draw(path.get(49));
                map.drawRect(0, 0, 199, 199);
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 200);
            }
        };
        JButton hawaiiButton = new JButton() {
            Shape polygon;
            @Override
            public boolean contains(int x, int y) {
                if (polygon == null || !polygon.getBounds().equals(getBounds())) {
                    polygon = new Area(path.get(49));
                }
                return polygon.contains(x, y);
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 400);
            }
        };
        hawaiiButton.setOpaque(false);
        hawaiiButton.setContentAreaFilled(false);
        hawaiiButton.setBorderPainted(false);
        hawaiiPanel.add(hawaiiButton);
        hawaiiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame tempStateWindow = new Frame();
//                    JLabel stateNum = new JLabel(Integer.toString(finalK+1));
//                    tempStateWindow.add(stateNum);
                MapComponent mc = new MapComponent(true,50);
                JPanel stateViewPanel = new JPanel(new FlowLayout()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D county = (Graphics2D) g;
                        county.setColor(Color.GRAY);
                        county.fill(mc.getPaths());
                        county.setColor(tierListColor(tierList.get(((50) * 2 - 1) - 1)));
                        System.out.println("County 1 ranking: " + (tierList.get(((50) * 2 - 1) - 1)));
                        county.fill(mc.getCountyPaths().get(0));
                        county.setColor(tierListColor(tierList.get((50) * 2 - 1)));
                        System.out.println("County 2 ranking: " + (tierList.get((50) * 2 - 1)));
                        county.fill(mc.getCountyPaths().get(1));
                        county.setColor(Color.BLACK);
                        county.draw(mc.getPaths());
                        county.draw(mc.getCountyPaths().get(0));
                        county.draw(mc.getCountyPaths().get(1));
                    }
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(700, 700);
                    }
                };
                tempStateWindow.add(stateViewPanel);
                tempStateWindow.pack();
                tempStateWindow.setVisible(true);
            }
        });
//        hawaiiButton.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                stateSelected.setText("State Selected: " + stateNames.get(49) + "    ");
//            }
//
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                stateSelected.setText("State Selected: none    ");
//            }
//        });
        otherStatesPanel.add(hawaiiPanel);
        gbc.gridx = 5;
        gbc.gridy = 2;
        mainPanel.add(otherStatesPanel, gbc);

        // INITIALIZES THE MAINLAND U.S. PANEL
        JPanel USAPanel = new JPanel(new BorderLayout()) {
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
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 400);
            }
        };
        USAPanel.setIgnoreRepaint(true);
        for (int k = 0; k < 48; k++) {
            int finalK = k;
            JButton stateButton = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(new Color(0f,0f,0f,1f));
                }
                Shape polygon;
                @Override
                public boolean contains(int x, int y) {
                    if (polygon == null || !polygon.getBounds().equals(getBounds())) {
                        polygon = new Area(path.get(finalK));
                    }
                    return polygon.contains(x, y);
                }
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(800, 400);
                }
            };
            stateButton.setIgnoreRepaint(true);
            stateButton.setOpaque(true);
            stateButton.setContentAreaFilled(false);
            stateButton.setBorderPainted(false);
            USAPanel.add(stateButton);
            stateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Frame tempStateWindow = new Frame();
//                    JLabel stateNum = new JLabel(Integer.toString(finalK+1));
//                    tempStateWindow.add(stateNum);
                    MapComponent mc = new MapComponent(true,finalK+1);
                    JPanel stateViewPanel = new JPanel(new BorderLayout()) {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            Graphics2D county = (Graphics2D) g;
                            county.setColor(Color.GRAY);
                            county.fill(mc.getPaths());
                            county.setColor(tierListColor(tierList.get(((finalK + 1) * 2 - 1) - 1)));
                            System.out.println("County 1 ranking: " + (tierList.get(((finalK + 1) * 2 - 1) - 1)));
                            county.fill(mc.getCountyPaths().get(0));
                            county.setColor(tierListColor(tierList.get((finalK + 1) * 2 - 1)));
                            System.out.println("County 2 ranking: " + (tierList.get((finalK + 1) * 2 - 1)));
                            county.fill(mc.getCountyPaths().get(1));
                            county.setColor(Color.BLACK);
                            county.draw(mc.getPaths());
                            county.draw(mc.getCountyPaths().get(0));
                            county.draw(mc.getCountyPaths().get(1));
                        }
                        @Override
                        public Dimension getPreferredSize() {
                            return new Dimension(700, 700);
                        }
                    };
                    JButton countyButton1 = new JButton() {
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            Graphics2D county = (Graphics2D) g;
                            county.setColor(Color.CYAN);
                            county.fill(mc.getCountyPaths().get(0));
                        }
                        Shape polygon;
                        @Override
                        public boolean contains(int x, int y) {
                            if (polygon == null || !polygon.getBounds().equals(getBounds())) {
                                polygon = new Area(mc.getCountyPaths().get(0));
                            }
                            return polygon.contains(x, y);
                        }
                        @Override
                        public Dimension getPreferredSize() {
                            return new Dimension(700, 700);
                        }
                    };
                    countyButton1.setOpaque(false);
                    countyButton1.setContentAreaFilled(false);
                    countyButton1.setBorderPainted(false);
                    stateViewPanel.add(countyButton1, BorderLayout.CENTER);
                    countyButton1.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
//                            Frame tempContextWindow = new Frame();
//                            JPanel contextPanel = new JPanel(new FlowLayout()) {
//                                @Override
//                                public Dimension getPreferredSize() {
//                                    return new Dimension(400,400);
//                                }
//                            };
//                            contextPanel.add(new JLabel("blah"));
//                            tempContextWindow.add(contextPanel);
//                            tempContextWindow.pack();
//                            tempContextWindow.setVisible(true);
                            new PopUpWindow(((finalK + 1) * 2 - 1));
                        }
                    });
                    JButton countyButton2 = new JButton() {
                        Shape polygon;
                        @Override
                        public boolean contains(int x, int y) {
                            if (polygon == null || !polygon.getBounds().equals(getBounds())) {
                                polygon = new Area(mc.getCountyPaths().get(1));
                            }
                            return polygon.contains(x, y);
                        }
                        @Override
                        public Dimension getPreferredSize() {
                            return new Dimension(200, 200);
                        }
                    };
                    countyButton2.setOpaque(false);
                    countyButton2.setContentAreaFilled(false);
                    countyButton2.setBorderPainted(false);
                    stateViewPanel.add(countyButton2, BorderLayout.CENTER);
                    countyButton2.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
//                            Frame tempContextWindow = new Frame();
//                            JPanel contextPanel = new JPanel(new FlowLayout()) {
//                                @Override
//                                public Dimension getPreferredSize() {
//                                    return new Dimension(400,400);
//                                }
//                            };
//                            contextPanel.add(new JLabel("blah"));
//                            tempContextWindow.add(contextPanel);
//                            tempContextWindow.pack();
//                            tempContextWindow.setVisible(true);
                            new PopUpWindow(((finalK + 1) * 2 - 1) + 1);
                        }
                    });

                    tempStateWindow.add(stateViewPanel);
                    tempStateWindow.pack();
                    tempStateWindow.setVisible(true);
                }
            });
//            stateButton.addMouseListener(new java.awt.event.MouseAdapter() {
//                public void mouseEntered(java.awt.event.MouseEvent evt) {
//                    stateSelected.setText("State Selected: " + stateNames.get(finalK) + "    ");
//                }
//
//                public void mouseExited(java.awt.event.MouseEvent evt) {
//                    stateSelected.setText("State Selected: none    ");
//                }
//            });
        }
        gbc.gridx = 4;
        gbc.gridy = 9;
        gbc.weighty = 1.0;
        mainPanel.add(stateSelected, gbc);
        gbc.weighty = 0.0;

        gbc.gridx = 5;
        gbc.gridy = 1;
        mainPanel.add(USAPanel, gbc);

        // INITIALIZES LABELS
        JLabel label1 = new JLabel("Map");
        label1.setBounds(750, 100, 200, 100);
        label1.setFont(new Font("Serif",Font.PLAIN,20));
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
        this.add(mainPanel, BorderLayout.NORTH);
    }
    private void pressStateButton(int stateIndex) {

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
