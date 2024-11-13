package com.example.recipe.entity;

import jakarta.persistence.*;

@Entity // 테이블 이름을 user_table로 변경
public class Member {
    @Id
    @Column(name = "user_id")
    private String userId; // 로그인 사용시 아이디 (PK)

    private String userName; // 사용자 닉네임
    private String email; // 사용자 이메일
    private String password; // 로그인 비밀번호
    private String phone; // 사용자 전화번호
    private Grade grade; // 사용자 등급

    @Lob
    private byte[] profileImage;

    // Getters and Setters
    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public Member() {
        // 기본 생성자
    }

    public Member(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}

