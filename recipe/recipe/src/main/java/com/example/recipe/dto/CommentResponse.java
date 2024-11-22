package com.example.recipe.dto;

public class CommentResponse {
    private Long commentId;
    private Long recipeId;
    private String userId;
    private String userName;
    private String content;

    // Constructor
    public CommentResponse(Long commentId, Long recipeId, String userId, String userName, String content) {
        this.commentId = commentId;
        this.recipeId = recipeId;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
    }

    // Getters and setters
    public Long getCommentId() { return commentId; }
    public void setCommentId(Long commentId) { this.commentId = commentId; }

    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
