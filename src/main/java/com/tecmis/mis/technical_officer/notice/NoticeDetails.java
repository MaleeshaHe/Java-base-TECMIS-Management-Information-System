package com.tecmis.mis.technical_officer.notice;

public class NoticeDetails {
    private int notice_id;
    private String title;
    private String date;
    private String time;
    private String content;



    public NoticeDetails(int notice_id, String title, String date, String time, String content) {
        this.notice_id = notice_id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.content = content;
    }


    public int getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(int notice_id) {
        this.notice_id = notice_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
