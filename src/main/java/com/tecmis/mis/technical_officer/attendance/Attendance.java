package com.tecmis.mis.technical_officer.attendance;

import java.time.LocalDate;

public class Attendance {
    String Course_Code;
    String Student_TG;
    String Attendance_State;
    String sDate;

    public void setCourse_Code(String course_Code) {
        Course_Code = course_Code;
    }

    public void setStudent_TG(String student_TG) {
        Student_TG = student_TG;
    }

    public void setAttendance_State(String attendance_State) {
        Attendance_State = attendance_State;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String getCourse_Code() {
        return Course_Code;
    }

    public String getStudent_TG() {
        return Student_TG;
    }

    public String getAttendance_State() {
        return Attendance_State;
    }

    public String getsDate() {
        return sDate;
    }

    public Attendance(String course_Code, String student_TG, String attendance_State, String sDate) {
        Course_Code = course_Code;
        Student_TG = student_TG;
        Attendance_State = attendance_State;
        this.sDate = sDate;
    }
}
