package com.example.quizcreator.Classes;

public class Question { //Question class with all constructors and getters/setters

    private int correctAnswer;
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;

    private Question() {
    }


    public Question(int correctAnswer, String answer1, String answer2) {
        this.correctAnswer = correctAnswer;
        this.answer1 = answer1;
        this.answer2 = answer2;

    }

    public Question(int correctAnswer,String answer1, String answer2, String answer3) {
        this.correctAnswer = correctAnswer;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
    }

    public Question(int correctAnswer,String answer1, String answer2, String answer3, String answer4) {
        this.correctAnswer = correctAnswer;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    private void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    private void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    private void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    private void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    private void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    private void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }
}
