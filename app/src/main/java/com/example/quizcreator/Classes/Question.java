package com.example.quizcreator.Classes;

public class Question { //Question class with all constructors and getters/setters

    private int correctAnswer;
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String userAnswer;

    public Question() {
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

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
