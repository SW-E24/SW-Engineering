package com.example.recipe.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId; // 각 댓글 고유 ID (PK)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member user; // 작성한 사용자 (FK)

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable=false)
    private Recipe recipe; // 어느 게시물에 달린 좋아요인지 (FK)

    private Long commentId; // 어느 댓글에 달린 좋아요인지 (FK)

    // Getters and Setters

    public Like() {
        // 기본 생성자
    }

    public Long getLikeId() {
        return likeId;
    }

    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    public Member getUser() {
        return user;
    }

    public void setUser(Member user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}

