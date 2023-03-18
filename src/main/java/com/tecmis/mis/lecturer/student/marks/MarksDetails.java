package com.tecmis.mis.lecturer.student.marks;

public class MarksDetails {
    private String tgnum;
    private String fname;
    private String lname;
    private String courseName;
    private String courseCode;
    private String examtype;
    private Float marks;
    private String examId;

    public MarksDetails(String examId,String tgnum, String fname, String lname, String courseName, String courseCode, String examtype, float marks) {
        this.examId=examId;
        this.tgnum=tgnum;
        this.fname=fname;
        this.courseName=courseName;
        this.courseCode=courseCode;
        this.examtype=examtype;
        this.marks=marks;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setTgnum(String tgnum) {
        this.tgnum = tgnum;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getTgnum() {
        return tgnum;
    }

    public String getFname() {
        return fname;
    }

    public Float getMarks() {
        return marks;
    }

    public void setMarks(Float marks) {
        this.marks = marks;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getExamtype() {
        return examtype;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setExamtype(String examtype) {
        this.examtype = examtype;
    }
}
