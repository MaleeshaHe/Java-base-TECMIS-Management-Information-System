package com.tecmis.mis.admin.user;

public class UserDetails {

    private int user_id;
    private String tgnum;
    private String fname;
    private String lname;
    private String phone_num;
    private String email;
    private String dob;
    private String sex;
    private String address;
    private String user_roll;
    private String short_name;

    public UserDetails(int user_id, String tgnum, String fname, String lname, String phone_num, String email, String dob, String sex, String address, String user_roll, String short_name) {
        this.user_id = user_id;
        this.tgnum = tgnum;
        this.fname = fname;
        this.lname = lname;
        this.phone_num = phone_num;
        this.email = email;
        this.dob = dob;
        this.sex = sex;
        this.address = address;
        this.user_roll = user_roll;
        this.short_name = short_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTgnum() {
        return tgnum;
    }

    public void setTgnum(String tgnum) {
        this.tgnum = tgnum;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_roll() {
        return user_roll;
    }

    public void setUser_roll(String user_roll) {
        this.user_roll = user_roll;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    //    private String name;
//    private String imgScr;
//    private String phoneNumber;
//    private String email;
//
//    public String getName() {
//
//        return name;
//    }
//
//    public void setName(String name) {
//
//        this.name = name;
//    }
//
//    public String getImgScr() {
//
//        return imgScr;
//    }
//
//    public void setImgScr(String imgScr) {
//
//        this.imgScr = imgScr;
//    }
//
//    public String getPhoneNumber() {
//
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//
//        this.phoneNumber = phoneNumber;
//    }
//
//    public String getEmail() {
//
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
}
