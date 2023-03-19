package com.tecmis.mis.student.grade_gpa;

public class GradeGAPDetails {
    private String courseCode;
    private String courseName;
    private float marks;



    public GradeGAPDetails(String courseCode, String courseName, float marks) {
        this.courseCode=courseCode;
        this.courseName=courseName;
        this.marks=marks;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }
    
    public void setMarks(float marks) {
        this.marks = marks;
    }

    public float getMarks() {
        return marks;
    }

    public String getGrade(){
        float marks = getMarks();
        if (marks >= 90) {
            return "A+";
        } else if (marks >= 85) {
            return "A";
        }else if (marks >= 80) {
            return "A-";
        } else if (marks >= 75) {
            return "B+";
        } else if (marks >= 70) {
            return "B";
        } else if (marks >= 65) {
            return "C+";
        }else if (marks >= 60) {
            return "C";
        }else if (marks >= 55) {
            return "D+";
        }else if (marks >= 50) {
            return "D";
        }else if (marks >= 40) {
            return "E";
        } else {
            return "F";
        }
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

}

