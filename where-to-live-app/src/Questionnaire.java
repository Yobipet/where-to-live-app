/*
Class: Questionnaire
Author: Samdine Murray, Dylan Potter, Henry Sanders, Nole Liu
Created: 10/6/2023
Last Modified: 11/2/2023

Purpose: Build Questionnaire window and initialize the instances of the Question class.

Attributes: -questionnaire: Frame
            -questionnaireProgress: boolean
            -answerTemplate: ArrayList<ArrayList<String>>()
            -answers: ArrayList<String>()
            -questionArrayList: ArrayList<String>()
            -questions: File
            -answerOptions: File

Methods:    +pressBack(): void
            +pressNext(): void
            +questionUI(): void
            +pullAnswers(): void
            +readAnswerList(): void
            +printQuestions(): String
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
    private Frame questionnaire = new Frame();
    private boolean questionnaireProgress;
    private ArrayList<ArrayList<String>> answerTemplate = new ArrayList<>();
    private ArrayList<String> answers = new ArrayList<>();
    private ArrayList<Question> questionArrayList = new ArrayList<>();
    private File questions = new File("Questions.txt");
    private File answerOptions = new File("AnswerOptions.csv");
    Questionnaire() {
        readAnswerList();
        questionnaireProgress = true;
        questionnaire.setSize(1600,1000);
        questionnaire.setLayout(null);
        Button b1 = new Button("BACK");
        b1.setBounds(1400, 700, 100, 50);
        questionnaire.add(b1);
        Button b2 = new Button("NEXT");
        b2.setBounds(1250, 700, 100, 50);
        questionnaire.add(b2);
        b1.addActionListener(this::pressBack);
        b2.addActionListener(this::pressNext);
        questionUI();
        System.out.println("Finished building questionnaire.");
        questionnaire.show();
    }
    private void pressBack(ActionEvent event) {
        pullAnswers();
        questionnaireProgress = false;
        questionnaire.hide();
    }
    private void pressNext(ActionEvent event) {
        pullAnswers();
        questionnaireProgress = false;
        questionnaire.hide();
        new Map();
    }
    private void questionUI() {
        int questionNumber = 1;
        int y = 50;
        ArrayList<JLabel> labels = new ArrayList<>();
        while (questionNumber <= 15) {
            if (questionNumber < 5) {
                questionArrayList.add(new Question(1, questionNumber,printQuestions(questionNumber -1),answerTemplate.get(questionNumber -1)));
                questionnaire.add(questionArrayList.get(questionNumber -1).getQuestionElement());
            }
            if (questionNumber >= 5 && questionNumber < 14) {
                questionArrayList.add(new Question(2, questionNumber,printQuestions(questionNumber -1),answerTemplate.get(questionNumber -1)));
                questionnaire.add(questionArrayList.get(questionNumber -1).getQuestionElement());
            }
            if (questionNumber == 14 || questionNumber == 15){
                questionArrayList.add(new Question(3, questionNumber,printQuestions(questionNumber -1),answerTemplate.get(questionNumber -1)));
                questionnaire.add(questionArrayList.get(questionNumber -1).getQuestionElement());
            }
            labels.add(new JLabel(printQuestions(questionNumber)));
            labels.get(questionNumber -1).setBounds(50, y, 600, 50);
            questionnaire.add(labels.get(questionNumber -1));
            System.out.println("Question " + (questionNumber) + " printed.");
            questionNumber++;
            y += 50;
        }
    }
    public void pullAnswers() {
        for (int i = 0; i < questionArrayList.size(); i++) {
            answers.add(questionArrayList.get(i).getAnswer());
        }
    }
    // Reads the AnswerOptions text file and returns a specific answer
    public void readAnswerList() {
        try {
            FileReader fr = new FileReader(answerOptions);
            BufferedReader br = new BufferedReader(fr);
            for (int i = 0;i < 15; i++) {
                String row = br.readLine();
                String[] cols = row.split(",");
                answerTemplate.add(new ArrayList<>());
                for (int j = 0;j < cols.length;j++) {
                    answerTemplate.get(i).add(cols[j]);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Reads the Questions text file and returns a specific question
    public String printQuestions(int questionNumber) {
        String row = "";
        try {
            FileReader fr = new FileReader(questions);
            BufferedReader br = new BufferedReader(fr);
            int i;
            for(i = 0; i < questionNumber; i++) {
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
    public boolean getQStatus() {
        return questionnaireProgress;
    }
}