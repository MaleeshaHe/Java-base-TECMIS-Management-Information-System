package com.tecmis.mis.admin.notice;

import com.tecmis.mis.admin.user.UserDetails;

public class NoticeDetails extends UserDetails {
    private int id;
    private String titel;
    private String date;
    private String time;

    public String getId() {
        return String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
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
