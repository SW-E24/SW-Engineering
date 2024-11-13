package com.example.recipe.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //기본키로 설정되고 자동 id값 생성
    private Long id; //고유 id

    @Column(nullable = false)
    private Long recipeId;
    //레시피 id 저장, 반드시 이 필드에 값이 있어야함

    @Column(nullable = false)
    private String userId;
    //사용자 id 저장, 반드시 값이 있어야함

    @Column(nullable = false)
    private String content;
    //댓글 내용, 반드시 값이 있어야함

    private LocalDateTime createdAt;
    //댓글이 작성된 시간, 현재시간으로 초기화
    private LocalDateTime updatedAt;
    //댓글이 마지막으로 수정된 시간, 기본생성자에서 현재 시간으로 초기화
    //content가 수정될때마다 업데이트됨

    public Comment() { //둘다 현재시간으로 초기화
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Comment(Long recipeId, String userId, String content) {
        this.recipeId = Long.valueOf(recipeId);
        this.userId = userId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and setters

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = Long.valueOf(String.valueOf(id));
    }

    public Long getRecipeId() { return recipeId; }

    public void setRecipeId(Long recipeId) {
        this.recipeId = Long.valueOf(String.valueOf(recipeId));
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) {
        this.userId = String.valueOf(userId);
    }

    public String getContent() { return content; }

    public void setContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
        //content 필드 값 업데이트, updateAt을 현시간으로 갱신함
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

}
