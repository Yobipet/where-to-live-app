/*
Class: Questionnaire
Author: Samdine Murray, Dylan Potter, Henry Sanders, Nole Liu
Created: 10/6/2023
Last Modified: 10/6/2023

Purpose:

Attributes: -answers: ArrayList<Objects>
            -questions: File

Methods: +answerListAdd(): void
         +printQuestions(): String
         +printAnswers(): String
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
public class Questionnaire {
    Frame q = new Frame();
    Label l;
    static JSlider s;
    static JComboBox c;
    static JTextField t;
    int questionNumber = 1;
    int x = 50;
    int y = 50;
    int w = 600;
    int h = 50;
    int min;
    int max;
    Questionnaire() {
        q.setSize(1600,800);
        Button b1 = new Button("BACK");
        b1.setBounds(1400, 700, 100, 50);
        q.add(b1);
        Button b2 = new Button("NEXT");
        b2.setBounds(1250, 700, 100, 50);
        q.add(b2);
        b1.addActionListener(this::pressBack);
        b2.addActionListener(this::pressNext);
        questionUI();
        q.show();
    }
    private void pressBack(ActionEvent e) {
        q.hide();
    }
    private void pressNext(ActionEvent e) {
        q.hide();
        new Map();
    }
    private void questionUI(){
        while (questionNumber < 20) {
            if (questionNumber < 5) {
                slider();
                q.add(s);
            }
            if (questionNumber >= 5 && questionNumber < 14) {
                select();
                q.add(c);
            }
            if (questionNumber >= 14 && questionNumber <= 15){
                text();
                q.add(t);
            }
            if (questionNumber > 15){

            }
            l = new Label(printQuestions(questionNumber));
            l.setBounds(x, y, w, h);
            q.add(l);
            questionNumber++;
            y += 50;
            if (y > 700) {
                x += 800;
                y = 50;
            }
        }
    }
    private void slider(){
        String temp = printAnswers(questionNumber);
        String[] line = temp.split(",");
        min = (int) Math.floor(Double.parseDouble(line[1]));
        max = (int) Math.ceil(Double.parseDouble(line[2]));
        int length = printQuestions(questionNumber).length();
        s = new JSlider(min, max, (min + max)/2);
        s.setBounds(x + 450,y,w / 2,40);
        System.out.println(length);
        s.setPaintTrack(true);
        s.setPaintTicks(true);
        s.setPaintLabels(true);
        s.setMajorTickSpacing((max-min)/5);
        s.setMinorTickSpacing((max-min)/10);
        s.addChangeListener(this::sliderReturn);
    }
    public int sliderReturn(ChangeEvent e){
        String temp = "";
        if (e.getSource() == s){
            answerListAdd(temp.valueOf(s.getValue()));
        }
        return 0;
    }
    private void select(){
        String temp = printAnswers(questionNumber);
        String[] line = temp.split(",");
        String choices[] = {line[1], line[2], line[3], line[4], line[5]};
        c = new JComboBox(choices);
        c.setBounds(x + 450, y, w / 2, 40);
        c.addItemListener(this::selectReturn);
    }
    public Object selectReturn(ItemEvent e){
        if (e.getSource() == c){
            answerListAdd(c.getSelectedItem().toString());
        }
        return null;
    }
    private void text(){
        t = new JTextField();
        t.setBounds(x + 450, y, w / 2, 40);
        t.addActionListener(this::textReturn);
    }
    public String textReturn(ActionEvent e){
        if (e.getSource() == t){
             answerListAdd(t.getText());
        }
        return null;
    }
    private ArrayList<String> answers = new ArrayList<String>();
    private File questions = new File("Questions.txt");
    private File answerOptions = new File("AnswerOptions.csv");

    public void answerListAdd(String answer) {
        answers.add(answer);
    }
    // Reads the AnswerOptions text file and returns a specific answer
    public String printAnswers(int questionNumber) {
        String row = "";
        try {
            FileReader fr = new FileReader(answerOptions);
            BufferedReader br = new BufferedReader(fr);
            for(int i = 0; i < questionNumber; i++) {
                row = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }
    // Reads the Questions text file and returns a specific question
    public String printQuestions(int questionNumber) {
        String row = "";
        try {
            FileReader fr = new FileReader(questions);
            BufferedReader br = new BufferedReader(fr);
            for(int i = 0; i < questionNumber; i++) {
                row = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }

    // Setters and Getters
    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }
    public ArrayList<String> getAnswers() {
        return answers;
    }
}
