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
    private JLabel stateSelected = new JLabel("  State Selected: none");
    private JLabel countySelected = new JLabel("  County Selected: none");
    private ArrayList<Path2D> path = new ArrayList<>();
//    private int pathLength;
//    private double[] coordinates = new double[2];
//    private File statePath = new File("C:/Users/henry/Desktop/FloridaPath.txt");
//    private int segmentNum;
    private ArrayList<MapComponent> mapPieces = new ArrayList<>();
    private ArrayList<Integer> stateTierList = new ArrayList<>();
    private ArrayList<Integer> tierList;
    private ArrayList<String> stateNames = new ArrayList<>();
    private ArrayList<ArrayList<String>> regionDatabase = new ArrayList<>();
    private boolean backFlag = false;
    public Map(ArrayList<Integer> masterTierList, ArrayList<String> stateNameList, ArrayList<ArrayList<String>> database) {
        // INITIALIZES ARRAYLIST VALUES
        this.tierList = masterTierList;
        this.stateNames = stateNameList;
        this.regionDatabase = database;
        for (int j = 0; j < tierList.size(); j+=2) {
            int avgTierValue = Math.round((tierList.get(j) + tierList.get(j+1))/2);
            stateTierList.add(avgTierValue);
        }
        // LOADS IN ALL MAPCOMPONENTS WITH PROPER INFORMATION
        for (int i = 1; i <= 50; i++) {
            mapPieces.add(new MapComponent(false, i));
            path.add(mapPieces.get(i-1).getPaths());
        }
        initializePanels();
    }
    private void initializePanels() {
        // INITIALIZES MAIN WINDOW
        this.setLayout(new BorderLayout());
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        GridBagConstraints gbc = new GridBagConstraints();
        stateSelected.setFont(new Font("Serif",Font.PLAIN,18));
        countySelected.setFont(new Font("Serif",Font.PLAIN,18));

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
                MapComponent mc = new MapComponent(true,49);
                JPanel stateViewPanel = new JPanel(new BorderLayout()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D scaledCounties = (Graphics2D) g;
                        scaledCounties.setColor(Color.GRAY);
                        scaledCounties.fill(mc.getPaths());
                        scaledCounties.setColor(Color.BLACK);
                        scaledCounties.draw(mc.getPaths());
                        scaledCounties.setColor(tierListColor(tierList.get(((49) * 2 - 1) - 1)));
                        scaledCounties.fill(mc.getCountyPaths().get(0));
                        scaledCounties.setColor(tierListColor(tierList.get((49) * 2 - 1)));
                        scaledCounties.fill(mc.getCountyPaths().get(1));
                        scaledCounties.setColor(Color.BLACK);
                        scaledCounties.draw(mc.getCountyPaths().get(0));
                        scaledCounties.draw(mc.getCountyPaths().get(1));
                    }
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(700, 700);
                    }
                };
                JButton countyButton1 = new JButton() {
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
                        new PopUpWindow(((49) * 2 - 1));
                    }
                });
                countyButton1.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered (java.awt.event.MouseEvent evt){
                        countySelected.setText("  County Selected: " + regionDatabase.get(((49) * 2 - 1) - 1).get(0));
                    }

                    public void mouseExited (java.awt.event.MouseEvent evt){
                        countySelected.setText("  County Selected: none");
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
                        return new Dimension(700, 700);
                    }
                };
                countyButton2.setOpaque(false);
                countyButton2.setContentAreaFilled(false);
                countyButton2.setBorderPainted(false);
                stateViewPanel.add(countyButton2, BorderLayout.CENTER);
                countyButton2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new PopUpWindow(((49) * 2 - 1) + 1);
                    }
                });
                countyButton2.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered (java.awt.event.MouseEvent evt){
                        countySelected.setText("  County Selected: " + regionDatabase.get((49) * 2 - 1).get(0));
                    }

                    public void mouseExited (java.awt.event.MouseEvent evt){
                        countySelected.setText("  County Selected: none");
                    }
                });
                stateViewPanel.add(countySelected, BorderLayout.SOUTH);
                tempStateWindow.setLayout(new BorderLayout());
                JLabel stateTitle = new JLabel(" " + stateNames.get(48));
                stateTitle.setFont(new Font("Serif",Font.PLAIN,20));
                tempStateWindow.add(stateTitle, BorderLayout.NORTH);
                tempStateWindow.add(stateViewPanel, BorderLayout.CENTER);
                JButton backButton = new JButton("BACK");
                backButton.setBounds(1400, 700, 100, 50);
                tempStateWindow.add(backButton, BorderLayout.SOUTH);
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tempStateWindow.hide();
                    }
                });
                tempStateWindow.pack();
                tempStateWindow.setVisible(true);
            }
        });
        alaskaButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                stateSelected.setText("  State Selected: " + stateNames.get(48));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                stateSelected.setText("  State Selected: none");
            }
        });
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
                MapComponent mc = new MapComponent(true,50);
                JPanel stateViewPanel = new JPanel(new BorderLayout()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D county = (Graphics2D) g;
                        county.setColor(Color.GRAY);
                        county.fill(mc.getPaths());
                        county.setColor(tierListColor(tierList.get(((50) * 2 - 1) - 1)));
                        county.fill(mc.getCountyPaths().get(0));
                        county.setColor(tierListColor(tierList.get((50) * 2 - 1)));
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
                        new PopUpWindow(((50) * 2 - 1));
                    }
                });
                countyButton1.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered (java.awt.event.MouseEvent evt){
                        countySelected.setText("  County Selected: " + regionDatabase.get(((50) * 2 - 1) - 1).get(0));
                    }

                    public void mouseExited (java.awt.event.MouseEvent evt){
                        countySelected.setText("  County Selected: none");
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
                        return new Dimension(700, 700);
                    }
                };
                countyButton2.setOpaque(false);
                countyButton2.setContentAreaFilled(false);
                countyButton2.setBorderPainted(false);
                stateViewPanel.add(countyButton2, BorderLayout.CENTER);
                countyButton2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new PopUpWindow(((50) * 2 - 1) + 1);
                    }
                });
                countyButton2.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered (java.awt.event.MouseEvent evt){
                        countySelected.setText("  County Selected: " + regionDatabase.get((50) * 2 - 1).get(0));
                    }

                    public void mouseExited (java.awt.event.MouseEvent evt){
                        countySelected.setText("  County Selected: none");
                    }
                });
                stateViewPanel.add(countySelected, BorderLayout.SOUTH);
                tempStateWindow.setLayout(new BorderLayout());
                JLabel stateTitle = new JLabel(" " + stateNames.get(49));
                stateTitle.setFont(new Font("Serif",Font.PLAIN,20));
                tempStateWindow.add(stateTitle, BorderLayout.NORTH);
                tempStateWindow.add(stateViewPanel, BorderLayout.CENTER);
                JButton backButton = new JButton("BACK");
                backButton.setBounds(1400, 700, 100, 50);
                tempStateWindow.add(backButton, BorderLayout.SOUTH);
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tempStateWindow.hide();
                    }
                });
                tempStateWindow.pack();
                tempStateWindow.setVisible(true);
            }
        });
        hawaiiButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                stateSelected.setText("  State Selected: " + stateNames.get(49));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                stateSelected.setText("  State Selected: none");
            }
        });
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
                    MapComponent mc = new MapComponent(true,finalK+1);
                    JPanel stateViewPanel = new JPanel(new BorderLayout()) {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            Graphics2D county = (Graphics2D) g;
                            county.setColor(Color.GRAY);
                            county.fill(mc.getPaths());
                            county.setColor(tierListColor(tierList.get(((finalK + 1) * 2 - 1) - 1)));
                            county.fill(mc.getCountyPaths().get(0));
                            county.setColor(tierListColor(tierList.get((finalK + 1) * 2 - 1)));
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
                            new PopUpWindow(((finalK + 1) * 2 - 1));
                        }
                    });
                    countyButton1.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseEntered (java.awt.event.MouseEvent evt){
                            countySelected.setText("  County Selected: " + regionDatabase.get(((finalK + 1) * 2 - 1) - 1).get(0));
                        }

                        public void mouseExited (java.awt.event.MouseEvent evt){
                            countySelected.setText("  County Selected: none");
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
                            return new Dimension(700, 700);
                        }
                    };
                    countyButton2.setOpaque(false);
                    countyButton2.setContentAreaFilled(false);
                    countyButton2.setBorderPainted(false);
                    stateViewPanel.add(countyButton2, BorderLayout.CENTER);
                    countyButton2.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new PopUpWindow(((finalK + 1) * 2 - 1) + 1);
                        }
                    });
                    countyButton2.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseEntered (java.awt.event.MouseEvent evt){
                            countySelected.setText("  County Selected: " + regionDatabase.get((finalK + 1) * 2 - 1).get(0));
                        }

                        public void mouseExited (java.awt.event.MouseEvent evt){
                            countySelected.setText("  County Selected: none");
                        }
                    });
                    stateViewPanel.add(countySelected, BorderLayout.SOUTH);
                    tempStateWindow.setLayout(new BorderLayout());
                    JLabel stateTitle = new JLabel(" " + stateNames.get(finalK));
                    stateTitle.setFont(new Font("Serif",Font.PLAIN,20));
                    tempStateWindow.add(stateTitle, BorderLayout.NORTH);
                    tempStateWindow.add(stateViewPanel, BorderLayout.CENTER);
                    JButton backButton = new JButton("BACK");
                    backButton.setBounds(1400, 700, 100, 50);
                    tempStateWindow.add(backButton, BorderLayout.SOUTH);
                    backButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            tempStateWindow.hide();
                        }
                    });
                    tempStateWindow.pack();
                    tempStateWindow.setVisible(true);
                }
            });
            stateButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    stateSelected.setText("  State Selected: " + stateNames.get(finalK));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    stateSelected.setText("  State Selected: none");
                }
            });
        }
        USAPanel.add(stateSelected, BorderLayout.SOUTH);
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

    // SETTERS AND GETTERS
    public boolean getBackFlag() {
        return backFlag;
    }
}
