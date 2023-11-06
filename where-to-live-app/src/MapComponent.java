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
//mostly incomplete, still working out how to reead excel file that stores coordinates
public class MapComponent {
    private Path2D[] paths = new Path2D[250];
    private Color[] colors = new Color[250];
    private int pathLength;
    /*
    public MapComponent() {
        createPathsAndColors();

    }
    public void createPathsAndColors(){
        for (int i = 0; i < 250; i++){
            paths[i] = new Path2D.Double();
            createPath(paths[i]);
            colors[i] = chooseColor();
        }
        paths.add(path);
    }
    public void createPath(Path2D path){
        int[] coordinates = new int[2];
        int segmentNum = 0;
        path.moveTo(coordinates[0],coordinates[1]);
        segmentNum++;
        while (segmentNum < pathLength) {
            getStatePath();
            path.lineTo(coordinates[0], coordinates[1]);
            segmentNum++;
        }
        path.closePath();

    }
    public Color chooseColor(){
        Color tempColor = Color.RED;

        return tempColor;
    }
    public static ArrayList<String> readColumnTwoFromExcel(String filePath) {
        ArrayList<String> columnTwoData = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(new File(filePath))) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0); // Assuming you want the data from the first sheet

            for (Row row : sheet) {
                Cell cell = row.getCell(1); // Index 1 represents the second column (0-based index)
                if (cell != null) {
                    columnTwoData.add(cell.getStringCellValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return columnTwoData;
    }
    public void getStatePath() {
        String row = "";
        try {
            FileReader fr = new FileReader("C:/Users/henry/Desktop/CoordinateData.xlsx");
            BufferedReader br = new BufferedReader(fr);
            row = br.readLine();
            String[] splitLine = row.split(" ");
            pathLength = splitLine.length;
            for(int i = 0; i < questionNumber; i++) {
                row = br.readLine();
            }
            for (int i = 0; i < pathLength; i++) {
                String[] tempLine = completeLine[i].split(",");
                double tempX = Double.parseDouble(tempLine[0]);
                double tempY = Double.parseDouble(tempLine[1]);
                if (tempX < minX) {
                    minX = tempX;
                }
                if (tempX > maxX) {
                    maxX = tempX;
                }
                if (tempY < minY) {
                    minY = tempY;
                }
                if (tempY > maxY) {
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

     */
}
