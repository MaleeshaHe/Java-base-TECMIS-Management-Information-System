package com.tecmis.mis.lecturer.student;

public class LecturerStudentDetails {
    private String tgnum;
    private String fname;
    private String lname;
    private String phoneNum;
    public LecturerStudentDetails(String tgnum, String fname, String lname, String phoneNum) {
        this.tgnum=tgnum;
        this.fname=fname;
        this.lname=lname;
        this.phoneNum=phoneNum;
    }

    public String getTgnum(){
        return tgnum;
    }

    public void setTgnum(String tgnum){
        this.tgnum=tgnum;
    }

    public String getFname(){
        return fname;
    }

    public void setFname(String fname){
        this.fname=fname;
    }

    public String getLname(){
        return lname;
    }

    public void setLname(String lname){
        this.lname=lname;
    }
    public String getPhoneNum(){
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum){
        this.phoneNum=phoneNum;
    }
}
