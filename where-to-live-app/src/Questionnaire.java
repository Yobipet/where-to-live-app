import java.awt.*;
/*
Class: Questionnaire
Author: Samdine Murray, Dylan Potter, Henry Sanders, Nole Liu
Created: 10/6/2023
Last Modified: 10/6/2023

Purpose:

Attributes: -answers: int[]

Methods: +answerListAdd(): void
 */
public class Questionnaire {
    Frame q = new Frame();
    Questionnaire() {
        q.setSize(1920,1080);
        q.show();
        questionUI();
    }
    int questionNumber = 0;
    int x = 50;
    int y = 0;
    int w = 100;
    int h = 50;
    Label l;
    private void questionUI(){
        while (questionNumber < 16) {
            l = new Label(printQuestion(questionNumber));
            l.setBounds(x, y, w, h);
            q.add(new Label("question" + questionNumber + 1));
            questionNumber++;
            y += 50;
        }
    }
    private int[] answers;

    public void answerListAdd() {

    }

    // Setters and Getters
    public void setAnswers(int[] answers) {
        this.answers = answers;
    }
    public int[] getAnswers() {
        return answers;
    }
}
