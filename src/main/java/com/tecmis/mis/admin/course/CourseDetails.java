package com.tecmis.mis.admin.course;

public class CourseDetails {
    private String courseCode;
    private String courseName;
    private String courseType;
    private int credit;
    private String material;
    private String cDepName;

    public CourseDetails(String courseCode, String courseName, String courseType, int credit, String material, String cDepName) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseType = courseType;
        this.credit = credit;
        this.material = material;
        this.cDepName = cDepName;
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

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getcDepName() {
        return cDepName;
    }

    public void setcDepName(String cDepName) {
        this.cDepName = cDepName;
    }
}
