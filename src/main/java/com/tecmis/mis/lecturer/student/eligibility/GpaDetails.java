package com.tecmis.mis.lecturer.student.eligibility;

public class GpaDetails {
    private String tgnum;
    private String fname;
    private float marks;
    private float cgpa;
    private float sgpa;

    public GpaDetails(String tgnum, String fname, float marks) {
        this.tgnum=tgnum;
        this.fname=fname;
        this.marks=marks;
    }

    public float getMarks() {
        return marks;
    }

    public String getFname() {
        return fname;
    }

    public String getTgnum() {
        return tgnum;
    }
    public float getCgpa() {

        return 2.5F;
    }

    public float getSgpa() {
        return 3.85F;
    }
}
