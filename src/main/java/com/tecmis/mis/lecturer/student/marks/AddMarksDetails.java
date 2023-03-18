package com.tecmis.mis.lecturer.student.marks;

public class AddMarksDetails {
    String examid;
    public AddMarksDetails(String examid) {
        this.examid=examid;
    }

    public String getExamid() {
        return examid;
    }

    public void setExamid(String examid) {
        this.examid = examid;
    }
}
