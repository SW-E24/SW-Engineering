package com.example.recipe.dto;

import com.example.recipe.entity.Recipe;
import java.util.List;

public class RecipeRequest {
    private String title;
    private String category;
    private List<Recipe.Ingredient> ingredients;
    private List<Recipe.Step> steps;
    private String description;

    // 기본 생성자
    public RecipeRequest() {}

    // 전체 필드 생성자
    public RecipeRequest(String title, String category, List<Recipe.Ingredient> ingredients,
                         List<Recipe.Step> steps, String description) {
        this.title = title;
        this.category = category;
        this.ingredients = ingredients;
        this.steps = steps;
        this.description = description;
    }

    // Getter/Setter 메소드들
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Recipe.Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Recipe.Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Recipe.Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Recipe.Step> steps) {
        this.steps = steps;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
