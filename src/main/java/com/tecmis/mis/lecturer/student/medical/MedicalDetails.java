package com.tecmis.mis.lecturer.student.medical;

public class MedicalDetails {
    private int m_id;
    private String tgnum;
    private String fname;
    private String lname;
    private String m_title;
    private String startDate;
    private String endDate;
    private byte[] documents;
    public MedicalDetails(int m_id,String tgnum, String fname,String lname, String m_title, String startDate, String endDate, byte[] documents) {
        this.m_id = m_id;
        this.tgnum=tgnum;
        this.fname=fname;
        this.lname=lname;
        this.m_title = m_title;
        this.startDate=startDate;
        this.endDate=endDate;
        this.documents=documents;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLname() {
        return lname;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public String getFname(){
        return fname;
    }
    public String getTgnum(){
        return tgnum;
    }
    public String getM_title() {
        return m_title;
    }
    public String getStartDate(){
        return startDate;
    }
    public String getEndDate(){
        return endDate;
    }
    public void setTgnum(String tgnum){
        this.tgnum=tgnum;
    }
    public void setFname(String fname){
        this.tgnum=tgnum;
    }
    public void setM_title(String m_title) {
        this.m_title = m_title;
    }
    public void setStartDate(String startDate){
        this.startDate=startDate;
    }
    public void setEndDate(String endDate){
        this.endDate=endDate;
    }
    public void setDocuments(byte[] documents){
        this.documents=documents;
    }
}
