package com.example.recipe.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;
    private LocalDate date;

    @ElementCollection
    private List<Ingredient> ingredients;

    @ElementCollection
    private List<String> steps;

    private String description;

    public Recipe() {}

    public Recipe(String title, String category, LocalDate date, List<Ingredient> ingredients, List<String> steps, String description) {
        this.title = title;
        this.category = category;
        this.date = date;
        this.ingredients = ingredients;
        this.steps = steps;
        this.description = description;
    }

    // Getter 및 Setter
    public Long getId() {
        return id;
    }

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Embeddable
    public static class Ingredient {
        private String name;
        private String quantity;

        public Ingredient() {}

        public Ingredient(String name, String quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }
    }
}