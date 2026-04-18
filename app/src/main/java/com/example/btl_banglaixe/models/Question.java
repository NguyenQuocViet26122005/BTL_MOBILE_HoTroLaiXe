package com.example.btl_banglaixe.models;

public class Question {
    private int id;
    private String questionText, optionA, optionB, optionC, optionD, correctAnswer, category, imagePath, explanation;
    private boolean isCritical;

    public Question() {}

    public Question(int id, String questionText, String optionA, String optionB, String optionC, String optionD, 
                   String correctAnswer, String category, boolean isCritical, String imagePath, String explanation) {
        this.id = id;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.category = category;
        this.isCritical = isCritical;
        this.imagePath = imagePath;
        this.explanation = explanation;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }
    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }
    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }
    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean isCritical() { return isCritical; }
    public void setCritical(boolean critical) { isCritical = critical; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}
