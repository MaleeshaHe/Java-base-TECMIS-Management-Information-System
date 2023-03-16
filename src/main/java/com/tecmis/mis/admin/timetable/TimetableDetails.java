package com.tecmis.mis.admin.timetable;

public class TimetableDetails {
    private int tid;
    private String title;
    private int depId;
    private String short_name;
    private int level;
    private byte[] pdffile;

    public TimetableDetails(int tid, String title, int depId, String short_name, int level, byte[] pdffile) {
        this.tid = tid;
        this.title = title;
        this.depId = depId;
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

    public int getDepId() {
        return depId;
    }

    public void setDepId(String depName) {
        this.depId = depId;
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
