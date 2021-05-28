package com.aid.sms723.model;

public class UserData {

    private String userId;
    private String username;
    private String mail;
    private String confirmCod;
    private String password;
    private String phone;
    private String limit;

    public UserData() {
    }
    public UserData(String limit){
        this.limit = limit;
    }

    public UserData(String mail, String password) {
        this.username = username;
        this.mail = mail;
        this.password = password;
    }

    public UserData(String userId, String username, String mail, String confirmCod, String password, String phone, String limit) {
        this.userId = userId;
        this.username = username;
        this.mail = mail;
        this.confirmCod = confirmCod;
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

    public String getconfirmCod() {
        return confirmCod;
    }

    public void setconfirmCod(String confirmCod) {
        this.confirmCod = confirmCod;
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
