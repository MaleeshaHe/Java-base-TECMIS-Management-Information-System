package com.tecmis.mis.admin.notice;

import com.tecmis.mis.admin.user.UserDetails;

public class NoticeDetails extends UserDetails {
    private int notice_id;
    private String title;
    private String date;
    private String time;

    public NoticeDetails(int notice_id, String title, String date, String time) {
        this.notice_id = notice_id;
        this.title = title;
        this.date = date;
        this.time = time;
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
}
