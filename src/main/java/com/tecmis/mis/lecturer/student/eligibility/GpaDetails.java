package com.tecmis.mis.lecturer.student.eligibility;

public class GpaDetails {
    private String tgnum;
    private String fname;
    private float sgpa;
    private float cgpa;

    public GpaDetails(String tgnum, String fname, float sgpa) {
        this.tgnum=tgnum;
        this.fname=fname;
        this.sgpa=sgpa;
    }

    public float getMarks() {
        return sgpa;
    }

    public String getFname() {
        return fname;
    }

    public String getTgnum() {
        return tgnum;
    }
    public float getCgpa() {
        return sgpa;
    }

    public float getSgpa() {
        return sgpa;
    }
}
