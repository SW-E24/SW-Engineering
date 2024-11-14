package com.example.recipe.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_table")
public class Member {
    @Id

    // 속성 정의
    @Column(name = "user_id")
    private String userID;
    private String userPW;
    private String userName;
    private String userEmail;
    private String userPhone;
    @Lob
    private byte[] profileImage;

    // 생성자
    public Member(){}
    public Member(String id, String password, String name, String email, String phone) {
        this.userID = id;
        this.userPW = password;
        this.userName = name;
        this.userEmail = email;
        this.userPhone = phone;
    }

    //Getter and Setter
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPW() {
        return userPW;
    }

    public void setUserPW(String userPW) {
        this.userPW = userPW;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }
}
