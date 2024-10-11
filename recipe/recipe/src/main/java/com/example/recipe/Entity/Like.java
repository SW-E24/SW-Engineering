package com.example.recipe.Entity;

import jakarta.persistence.*;

@Entity
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId; // 각 댓글 고유 ID (PK)

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 작성한 사용자 (FK)

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe; // 어느 게시물에 달린 좋아요인지 (FK)

    private Long commentId; // 어느 댓글에 달린 좋아요인지 (FK)

    // Getters and Setters
    public Long getLikeId() {
        return likeId;
    }

    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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