package com.example.recipe.entity;

import jakarta.persistence.*;

@Entity
public class View {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewId; // 각 조회수 고유 ID (PK)

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe; // 어느 게시글의 조회수인지 (FK)

    private int countView; // 조회수 수치

    // Getters and Setters
    public Long getViewId() {
        return viewId;
    }

    public void setViewId(Long viewId) {
        this.viewId = viewId;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public int getCountView() {
        return countView;
    }

    public void setCountView(int countView) {
        this.countView = countView;
    }
}

