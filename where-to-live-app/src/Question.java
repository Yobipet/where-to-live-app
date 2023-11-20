/*
Class: Question
Author: Samdine Murray, Dylan Potter, Henry Sanders, Nole Liu
Created: 11/1/2023
Last Modified: 11/2/2023

Purpose: General class to initialize questions for questionnaire. Holds variables
         and handles answers for questionnaire.

Attributes: -qNum: int
            -qText: String
            -qAnswers: ArrayList<String>
            -type: int
            -chosenAnswer: String
            -question: JComponent

Methods:    +slider(): void
            +select(): void
            +text(): void
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Question {
    private int qNum;
    private String qText;
    private ArrayList<String> qAnswers = new ArrayList<>();
    private int type;
    private String chosenAnswer;
    private JComponent question;
    Question(int type, int qNum, String qText, ArrayList<String> qAnswers) {
        System.out.println(qAnswers);
        this.qAnswers = qAnswers;
        this.qNum = qNum;
        this.type = type;
        this.qText = qText;
        this.chosenAnswer = qAnswers.get(1);
        if (type == 1) {
            this.chosenAnswer = String.valueOf((Double.parseDouble(qAnswers.get(1)) + Double.parseDouble((qAnswers.get(2))) / 2));
            slider();
        }
        else if (type == 2) {
            select();
        }
        else {
            text();
        }
    }
    private void slider() {
        int min = (int) Math.floor(Double.parseDouble(qAnswers.get(1)));
        int max = (int) Math.ceil(Double.parseDouble(qAnswers.get(2)));
        JSlider question = new JSlider(min, max, (min + max)/2);
        question.setBounds(600,qNum * 50,600 / 2,40);
        // System.out.println(length);
        question.setPaintTrack(true);
        question.setPaintTicks(true);
        question.setPaintLabels(true);
        question.setMajorTickSpacing((max-min)/5);
        question.setMinorTickSpacing((max-min)/10);
        question.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                chosenAnswer = String.valueOf(question.getValue());
            }
        });
        this.question = question;
    }
    private void select() {
        String choices[];
        if (qAnswers.get(3).equals("null")) {
            choices = new String[]{qAnswers.get(1), qAnswers.get(2)};
        }
        else if (qAnswers.get(4).equals("null")) {
            choices = new String[]{qAnswers.get(1), qAnswers.get(2), qAnswers.get(3)};
        }
        else {
            choices = new String[]{qAnswers.get(1), qAnswers.get(2), qAnswers.get(3), qAnswers.get(4), qAnswers.get(5)};
        }
        JComboBox question = new JComboBox(choices);
        question.setBounds(600,qNum * 50,600 / 2,40);
        question.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                chosenAnswer = question.getSelectedItem().toString();
            }
        });
        this.question = question;
    }
    private void text(){
        JTextField question = new JTextField();
        question.setBounds(600,qNum * 50,600 / 2,40);
        question.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent event) {
            }
            public void focusLost(FocusEvent event) {
                chosenAnswer = question.getText();
            }
        });
        this.question = question;
    }

    // SETTERS & GETTERS
    public JComponent getQuestionElement() {
        return this.question;
    }
    public String getAnswer() {
        return chosenAnswer;
    }
}
