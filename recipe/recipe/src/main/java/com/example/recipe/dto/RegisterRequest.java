package com.example.recipe.dto;

public class RegisterRequest {
    private String userId;
    private String userName;
    private String email;
    private String phone;
    private String password;
    private String confirmuserPW;  // 비밀번호 확인 필드

    // Getter, Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmuserPW() {
        return confirmuserPW;
    }

    public void setConfirmuserPW(String confirmuserPW) {
        this.confirmuserPW = confirmuserPW;
    }
}
