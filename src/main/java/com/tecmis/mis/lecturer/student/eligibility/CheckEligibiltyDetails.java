package com.tecmis.mis.lecturer.student.eligibility;

public class CheckEligibiltyDetails {
    private  String tgnum;
    private String fname;
    private String courseName;
    private float attendanceCount;
    private float caMarks;
    private String courseCode;
    public CheckEligibiltyDetails(String tgnum, String fname, String courseName,String courseCode, float attendanceCount, float caMarks) {
        this.tgnum=tgnum;
        this.fname=fname;
        this.courseCode=courseCode;
        this.courseName=courseName;
        this.attendanceCount=attendanceCount;
        this.caMarks=caMarks;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getFname() {
        return fname;
    }

    public String getTgnum() {
        return tgnum;
    }

    public float getAttendanceCount() {
        return Math.round((attendanceCount/15)*100);
    }

    public float getCaMarks() {
        if(courseCode.equals("ICT01") || courseCode.equals("ICT04") || courseCode.equals(("ICT03"))){
            return Math.round((caMarks/40)*100);
        }else{
            return Math.round((caMarks/70)*100);
        }
    }
    public String getStatus() {
        if(courseCode.equals("ICT01") || courseCode.equals("ICT04") || courseCode.equals(("ICT03"))){
            return (caMarks/40)*100 >= 50 && (attendanceCount/15)*100 >=80 ? "Pass" : "Fail";
        }else{
            return (caMarks/70)*100 >= 50 && (attendanceCount/15)*100 >=80 ? "Pass" : "Fail";
        }

    }
}
