package com.tecmis.mis.student.course;

public class CourseDetails {
    private int cid;
    private String courseCode;
    private String courseName;
    private int credit;
    private String material;

    public CourseDetails(int cid, String courseCode, String courseName, int credit, String material) {
        this.cid = cid;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credit = credit;
        this.material = material;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
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

}
