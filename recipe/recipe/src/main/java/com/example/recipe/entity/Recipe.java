package com.example.recipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Bookmark> bookmarks;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Like> likes;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<View> views;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;




    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    @Column(length = 300, nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDateTime date;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<Ingredient> ingredients;

    @ElementCollection
    @CollectionTable(name = "recipe_steps", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<Step> steps;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Recipe() {}
    
    @Builder
    public Recipe(Long recipeId, Member user, String title, String category, LocalDateTime date,
                  List<Ingredient> ingredients, List<Step> steps, String description) {
          this.recipeId = recipeId;
          this.user = user;
          this.title = title;
          this.category = category;
          this.date = date;
          this.ingredients = ingredients;
          this.steps = steps;
          this.description = description;
    }
    
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
    
    @Getter
    @Setter
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
    @Getter
    @Setter
    public static class Step {
        private String description;

        @Lob
        @Column(columnDefinition = "BLOB")
        private byte[] photo; // LOB 방식으로 이미지 저장

        @Transient
        private String imageUrl; // 동적 URL 관리

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
