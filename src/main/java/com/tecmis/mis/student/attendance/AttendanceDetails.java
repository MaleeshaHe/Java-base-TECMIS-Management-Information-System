package com.tecmis.mis.student.attendance;

public class AttendanceDetails {
    private String courseCode;
    private String courseName;
    private String tgNum;
    private String date;
    private String state;

    public AttendanceDetails(String courseCode, String courseName, String tgNum, String date, String state) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.tgNum = tgNum;
        this.date = date;
        this.state = state;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTgNum() {
        return tgNum;
    }

    public void setTgNum(String tgNum) {
        this.tgNum = tgNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
