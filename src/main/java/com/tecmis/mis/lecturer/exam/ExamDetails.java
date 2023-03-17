package com.tecmis.mis.lecturer.exam;

public class ExamDetails{
    private String ExamId;
    private String courseName;
    private int ExamScore;
    private String examType;

    public ExamDetails(String ExamId, String courseName, String examType, int examScore) {
        this.ExamId = ExamId;
        this.courseName = courseName;
        this.examType = examType;
        this.ExamScore = examScore;
    }
    public String getExamId(){
        return ExamId;
    }
    public void setExamScore(int ExamScore) {
        this.ExamScore = ExamScore;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public void setExamType(String examType) {
        this.examType = examType;
    }
    public String getexamId(){
        return ExamId;
    }
    public String getCourseName() {
        return courseName;
    }
    public String getExamType(){return examType;}
    public int getExamScore(){return ExamScore;}

}
