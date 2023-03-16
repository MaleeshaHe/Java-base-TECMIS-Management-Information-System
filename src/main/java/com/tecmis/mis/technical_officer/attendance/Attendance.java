package com.tecmis.mis.technical_officer.attendance;

import java.time.LocalDate;

public class Attendance {
    private String Course_Code;
    private String Student_TG;
    private String Attendance_State;
    private String AttDate;

    public Attendance(String course_Code, String student_TG, String attendance_State, String attDate) {
        Course_Code = course_Code;
        Student_TG = student_TG;
        Attendance_State = attendance_State;
        AttDate = attDate;
    }

    public String getCourse_Code() {
        return Course_Code;
    }

    public void setCourse_Code(String course_Code) {
        Course_Code = course_Code;
    }

    public String getStudent_TG() {
        return Student_TG;
    }

    public void setStudent_TG(String student_TG) {
        Student_TG = student_TG;
    }

    public String getAttendance_State() {
        return Attendance_State;
    }

    public void setAttendance_State(String attendance_State) {
        Attendance_State = attendance_State;
    }

    public String getAttDate() {
        return AttDate;
    }

    public void setAttDate(String attDate) {
        AttDate = attDate;
    }

}
