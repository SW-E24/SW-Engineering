package com.example.recipe.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_table")
public class Member {
    @Id

    // 속성 정의
    @Column(name = "userid")
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;
    @Lob
    private byte[] profileImage;

    // 생성자
    public Member(){}
    public Member(String id, String password, String name, String email, String phone) {
        this.userId = id;
        this.password = password;
        this.userName = name;
        this.email = email;
        this.phone = phone;
    }

    //Getter and Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userID) {
        this.userId = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String userPW) {
        this.password = userPW;
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

    public void setEmail(String userEmail) {
        this.email = userEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String userPhone) {
        this.phone = userPhone;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

}
