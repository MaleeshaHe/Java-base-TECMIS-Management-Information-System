package com.tecmis.mis.lecturer.course;

public class CourseDetails {
    private static String courseCode;
    private static String courseName;
    private static int credit;
    private static String material;

    public CourseDetails( String courseCode, String courseName, int credit, String material) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credit = credit;
        this.material = material;
    }


    public static String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public static String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public static int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public static String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

}
