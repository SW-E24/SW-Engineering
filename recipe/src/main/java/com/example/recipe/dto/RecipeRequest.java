// com.example.recipe.dto.RecipeRequest.java
package com.example.recipe.dto;

import com.example.recipe.entity.Recipe.Ingredient;

import java.util.List;

public class RecipeRequest {
    private Long recipeId; // 추가된 필드
    private String title;
    private String category;
    private List<Ingredient> ingredients;
    private List<String> steps;
    private String description;

    // Getters and setters
    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public List<String> getSteps() { return steps; }
    public void setSteps(List<String> steps) { this.steps = steps; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}