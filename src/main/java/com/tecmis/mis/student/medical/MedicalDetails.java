package com.tecmis.mis.student.medical;

public class MedicalDetails {
    private int m_id;
    private String m_title;
    private String m_description;
    private String start_date;
    private String end_date;
    private String students_tg;

    public MedicalDetails(String m_title, String m_description, String start_date, String end_date, int m_id, String students_tg) {
        this.m_title = m_title;
        this.m_description = m_description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.m_id = m_id;
        this.students_tg = students_tg;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public String getM_title() {
        return m_title;
    }

    public void setM_title(String m_title) {
        this.m_title = m_title;
    }

    public String getM_description() {
        return m_description;
    }

    public void setM_description(String m_description) {
        this.m_description = m_description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStudents_tg() {
        return students_tg;
    }

    public void setStudents_tg(String students_tg) {
        this.students_tg = students_tg;
    }
}
