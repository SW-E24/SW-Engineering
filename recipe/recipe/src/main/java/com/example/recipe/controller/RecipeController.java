package com.example.recipe.controller;

import com.example.recipe.dto.RecipeRequest;
import com.example.recipe.entity.Recipe;
import com.example.recipe.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/create")
    public Recipe createRecipe(@RequestBody RecipeRequest recipeRequest) {
        if (!recipeRequest.getCategory().matches("양식|한식|중식|일식|디저트")) {
            throw new IllegalArgumentException("카테고리는 양식, 한식, 중식, 일식, 디저트 중 하나여야 합니다.");
        }
        return recipeService.createRecipe(
                recipeRequest.getTitle(),
                recipeRequest.getCategory(),
                recipeRequest.getIngredients(),
                recipeRequest.getSteps(),
                recipeRequest.getDescription()
        );
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long recipeId) {
        Optional<Recipe> recipeOpt = recipeService.getRecipeById(recipeId);
        return recipeOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long recipeId, @RequestBody RecipeRequest recipeRequest) {
        try {
            Recipe recipe = recipeService.updateRecipe(recipeId, recipeRequest);
            return ResponseEntity.ok(recipe);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long recipeId) {
        Optional<Recipe> recipeOpt = recipeService.getRecipeById(recipeId);
        if (recipeOpt.isPresent()) {
            recipeService.deleteRecipe(recipeId);
            return ResponseEntity.ok("삭제 되었습니다");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
