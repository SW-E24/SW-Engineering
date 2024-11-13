package com.example.recipe.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member user;
    private String title;
    private String category;
    private LocalDateTime date;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<Ingredient> ingredients;

    @ElementCollection
    @CollectionTable(name = "recipe_steps", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<Step> steps; // Updated to use Step class

    private String description;

    public Recipe() {}

    public Recipe(String title, String category, LocalDateTime date, List<Ingredient> ingredients, List<Step> steps, String description) {
        this.title = title;
        this.category = category;
        this.date = date;
        this.ingredients = ingredients;
        this.steps = steps;
        this.description = description;
    }
    // Getters and setters...
    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = Long.valueOf(String.valueOf(recipeId)); }

    public Member getUser() {
        return user;
    }
    public void setUser(Member user) {
        this.user = user;
    }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date.atStartOfDay(); }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public List<Step> getSteps() { return steps; } // Updated getter
    public void setSteps(List<Step> steps) { this.steps = steps; } // Updated setter

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Embeddable
    public static class Ingredient {
        private String name;
        private String quantity;

        // Constructor, getters, and setters...
        public Ingredient() {}

        public Ingredient(String name, String quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getQuantity() { return quantity; }
        public void setQuantity(String quantity) { this.quantity = quantity; }
    }

    @Embeddable
    public static class Step {
        private String description;

        @Lob
        @Column(columnDefinition = "BLOB")
        private byte[] photo; // LOB 방식으로 이미지 저장

        public Step() {}

        public Step(String description, byte[] photo) {
            this.description = description;
            this.photo = photo;
        }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public byte[] getPhoto() { return photo; }
        public void setPhoto(byte[] photo) { this.photo = photo; }
    }
}
