import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
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
public class Questionnaire {
    private ArrayList<String> answers = new ArrayList<String>();
    private File questions = new File("Questions.txt");

    public void answerListAdd(String answer) {
        answers.add(answer);
    }
    // Reads the AnswerOptions text file and returns a specific answer
    public String printAnswers() {
        String row = "";


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
        questionNumber = 0; // Reset the counter every call, remove if doesn't affect anything
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
