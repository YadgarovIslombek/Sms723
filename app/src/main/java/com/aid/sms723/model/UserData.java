package com.aid.sms723.model;

public class UserData {

    private String userId;
    private String username;
    private String mail;
    private String imei;
    private String password;
    private String phone;
    private String limit;

    public UserData() {
    }

    public UserData(String userId, String username, String mail, String imei, String password, String phone, String limit) {
        this.userId = userId;
        this.username = username;
        this.mail = mail;
        this.imei = imei;
        this.password = password;
        this.phone = phone;
        this.limit = limit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
