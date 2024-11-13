package com.example.recipe.entity;

/*
* 회원 관리
* - 필요한 속성 : 아이디, 비밀번호, 이름, 이메일, 전화번호, 등급 */

import jakarta.persistence.*;

@Entity
@Table(name = "user_table")
public class Member {
    @Id

    // 속성 정의
    private String userID;
    private String userPW;
    private String userName;
    private String userEmail;
    private String userPhone;

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
}
