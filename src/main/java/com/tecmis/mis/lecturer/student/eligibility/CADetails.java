package com.tecmis.mis.lecturer.student.eligibility;

public class CADetails{
    private String tgnum;
    private String fname;
    private float marks;
    private String courseName;
    private String status;
    private String courseCode;
    public CADetails(String tgnum, String fname,String courseCode, String courseName, float marks) {
        this.tgnum=tgnum;
        this.fname=fname;
        this.courseCode=courseCode;
        this.courseName=courseName;
        this.marks=marks;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getStatus() {
        if(courseCode.equals("ICT01") || courseCode.equals("ICT04") || courseCode.equals(("ICT03"))){
            return (marks/40)*100 >= 50 ? "Pass" : "Fail";
        }else{
            return (marks/70)*100 >= 50 ? "Pass" : "Fail";
        }

    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFname() {
        return fname;
    }

    public void setMarks(float marks) {
        this.marks = marks;
    }

    public String getTgnum() {
        return tgnum;
    }

    public void setTgnum(String tgnum) {
        this.tgnum = tgnum;
    }

    public float getMarks() {
        return marks;
    }
}
