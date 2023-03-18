package com.tecmis.mis.lecturer.student.attendance;

public class AttendanceDetails {
    private String tgnum;
    private String fname;
    private String courseCode;
    private String date;
    private String state;
    private String lname;
    public AttendanceDetails(String tgnum, String fname, String courseCode, String date, String state, String lname) {
        this.tgnum=tgnum;
        this.fname=fname;
        this.courseCode=courseCode;
        this.date=date;
        this.state=state;
        this.lname=lname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getDate() {
        return date;
    }

    public String getFname() {
        return fname;
    }

    public String getState() {
        return state;
    }

    public String getTgnum() {
        return tgnum;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTgnum(String tgnum) {
        this.tgnum = tgnum;
    }
}
