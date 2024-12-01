package com.example.recipe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Grade {
    @Id
    // 속성 정의
    private String userID;

    @Column(nullable = false)
    private GradeType grade;

    private int postCount;
    private int commentCount;

    // 생성자
    public Grade() {}
    public Grade(String id, GradeType grade, int postCount, int commentCount) {
        this.userID = id;
        this.grade = grade;
        this.postCount = postCount;
        this.commentCount = commentCount;
    }

    // Getter & Setter
    public String getUserID() {
        return userID;
    }

    public GradeType getGrade() {
        return grade;
    }

    public void setGrade(GradeType grade) {
        this.grade = grade;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

}
