package com.tecmis.mis.admin.timetable;

public class TimetableDetails {
    private int tid;
    private String title;
    private String depName;
    private String short_name;
    private int level;
    private byte[] pdffile;

    public TimetableDetails(int tid, String title, String depName, String short_name, int level, byte[] pdffile) {
        this.tid = tid;
        this.title = title;
        this.depName = depName;
        this.short_name = short_name;
        this.level = level;
        this.pdffile = pdffile;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public byte[] getPdffile() {
        return pdffile;
    }

    public void setPdffile(byte[] pdffile) {
        this.pdffile = pdffile;
    }
}
