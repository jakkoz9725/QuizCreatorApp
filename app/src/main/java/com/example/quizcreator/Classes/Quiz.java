package com.example.quizcreator.Classes;

public class Quiz {
    private String quizName;
    private String creatorEmail;
    private String creatorUsername;


    public Quiz() {

    }

    public Quiz(String quizName, String creatorEmail, String creatorUsername) {
        this.quizName = quizName;
        this.creatorEmail = creatorEmail;
        this.creatorUsername = creatorUsername;
    }

    private Question Question1 = new Question();
    private Question Question2 = new Question();
    private Question Question3 = new Question();
    private Question Question4 = new Question();
    private Question Question5 = new Question();


    public void setQuestion1(String question, String answer1, String answer2, int correctAnswer) {
        Question1.setQuestion(question);
        Question1.setAnswer1(answer1);
        Question1.setAnswer2(answer2);
        Question1.setCorrectAnswer(correctAnswer);
    }

    public void setQuestion1(String question, String answer1, String answer2, String odpowiedz3, int correctAnswer) {
        Question1.setQuestion(question);
        Question1.setAnswer1(answer1);
        Question1.setAnswer2(answer2);
        Question1.setAnswer3(odpowiedz3);
        Question1.setCorrectAnswer(correctAnswer);
    }

    public void setQuestion1(String question, String answer1, String answer2, String odpowiedz3, String odpowiedz4, int correctAnswer) {
        Question1.setQuestion(question);
        Question1.setAnswer1(answer1);
        Question1.setAnswer2(answer2);
        Question1.setAnswer3(odpowiedz3);
        Question1.setAnswer4(odpowiedz4);
        Question1.setCorrectAnswer(correctAnswer);
    }

    ////////////////////////////////////////////////////////////

    public void setQuestion2(String question, String answer1, String answer2, int correctAnswer) {
        Question2.setQuestion(question);
        Question2.setAnswer1(answer1);
        Question2.setAnswer2(answer2);
        Question2.setCorrectAnswer(correctAnswer);
    }

    public void setQuestion2(String question, String answer1, String answer2, String odpowiedz3, int correctAnswer) {
        Question2.setQuestion(question);
        Question2.setAnswer1(answer1);
        Question2.setAnswer2(answer2);
        Question2.setAnswer3(odpowiedz3);
        Question2.setCorrectAnswer(correctAnswer);
    }

    public void setQuestion2(String question, String answer1, String answer2, String odpowiedz3, String odpowiedz4, int correctAnswer) {
        Question2.setQuestion(question);
        Question2.setAnswer1(answer1);
        Question2.setAnswer2(answer2);
        Question2.setAnswer3(odpowiedz3);
        Question2.setAnswer4(odpowiedz4);
        Question2.setCorrectAnswer(correctAnswer);
    }

    //////////////////////////////////////////////////////

    public void setQuestion3(String question, String answer1, String answer2, int correctAnswer) {
        Question3.setQuestion(question);
        Question3.setAnswer1(answer1);
        Question3.setAnswer2(answer2);
        Question3.setCorrectAnswer(correctAnswer);
    }

    public void setQuestion3(String question, String answer1, String answer2, String odpowiedz3, int correctAnswer) {
        Question3.setQuestion(question);
        Question3.setAnswer1(answer1);
        Question3.setAnswer2(answer2);
        Question3.setAnswer3(odpowiedz3);
        Question3.setCorrectAnswer(correctAnswer);
    }

    public void setQuestion3(String question, String answer1, String answer2, String odpowiedz3, String odpowiedz4, int correctAnswer) {
        Question3.setQuestion(question);
        Question3.setAnswer1(answer1);
        Question3.setAnswer2(answer2);
        Question3.setAnswer3(odpowiedz3);
        Question3.setAnswer4(odpowiedz4);
        Question3.setCorrectAnswer(correctAnswer);
    }

    public void setQuestion4(String question, String answer1, String answer2, int correctAnswer) {
        Question4.setQuestion(question);
        Question4.setAnswer1(answer1);
        Question4.setAnswer2(answer2);
        Question4.setCorrectAnswer(correctAnswer);
    }

    public void setQuestion4(String question, String answer1, String answer2, String odpowiedz3, int correctAnswer) {
        Question4.setQuestion(question);
        Question4.setAnswer1(answer1);
        Question4.setAnswer2(answer2);
        Question4.setAnswer3(odpowiedz3);
        Question4.setCorrectAnswer(correctAnswer);
    }

    public void setQuestion4(String question, String answer1, String answer2, String odpowiedz3, String odpowiedz4, int correctAnswer) {
        Question4.setQuestion(question);
        Question4.setAnswer1(answer1);
        Question4.setAnswer2(answer2);
        Question4.setAnswer3(odpowiedz3);
        Question4.setAnswer4(odpowiedz4);
        Question4.setCorrectAnswer(correctAnswer);
    }
    public void setQuestion5(String question, String answer1, String answer2, int correctAnswer) {
        Question5.setQuestion(question);
        Question5.setAnswer1(answer1);
        Question5.setAnswer2(answer2);
        Question5.setCorrectAnswer(correctAnswer);
    }

    public void setQuestion5(String question, String answer1, String answer2, String odpowiedz3, int correctAnswer) {
        Question5.setQuestion(question);
        Question5.setAnswer1(answer1);
        Question5.setAnswer2(answer2);
        Question5.setAnswer3(odpowiedz3);
        Question5.setCorrectAnswer(correctAnswer);
    }

    public void setQuestion5(String question, String answer1, String answer2, String odpowiedz3, String odpowiedz4, int correctAnswer) {
        Question5.setQuestion(question);
        Question5.setAnswer1(answer1);
        Question5.setAnswer2(answer2);
        Question5.setAnswer3(odpowiedz3);
        Question5.setAnswer4(odpowiedz4);
        Question5.setCorrectAnswer(correctAnswer);
    }
    /////////////////////////////////////////////////////////

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorName) {
        this.creatorEmail = creatorName;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public Question getQuestion1() {
        return Question1;
    }

    public void setQuestion1(Question Question1) {
        this.Question1 = Question1;
    }

    public Question getQuestion2() {
        return Question2;
    }

    public void setQuestion2(Question Question2) {
        this.Question2 = Question2;
    }

    public Question getQuestion3() {
        return Question3;
    }

    public void setQuestion3(Question Question3) {
        this.Question3 = Question3;
    }

    public Question getQuestion4() {
        return Question4;
    }

    public void setQuestion4(Question Question4) {
        this.Question4 = Question4;
    }

    public Question getQuestion5() {
        return Question5;
    }

    public void setQuestion5(Question Question5) {
        this.Question5 = Question5;
    }

}

