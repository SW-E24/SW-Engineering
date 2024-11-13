package com.example.recipe.controller;

import com.example.recipe.dto.RecipeRequest;
import com.example.recipe.entity.Recipe;
import com.example.recipe.service.GradeService;
import com.example.recipe.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final GradeService gradeService;    // 등급 업데이트를 위해 추가

    public RecipeController(RecipeService recipeService, GradeService gradeService) {
        this.recipeService = recipeService;
        this.gradeService = gradeService;
    }

    @PostMapping("/create")
    public Recipe createRecipe(@RequestBody RecipeRequest recipeRequest) {
        if (!recipeRequest.getCategory().matches("양식|한식|중식|일식|디저트")) {
            throw new IllegalArgumentException("카테고리는 양식, 한식, 중식, 일식, 디저트 중 하나여야 합니다.");
        }
        Recipe createdRecipe = recipeService.createRecipe(
                recipeRequest.getTitle(),
                recipeRequest.getCategory(),
                recipeRequest.getIngredients(),
                recipeRequest.getSteps(),
                recipeRequest.getDescription()
        );

        // 레시피 생성 후 postCount 증가
        String userID = "currentUserID";
        gradeService.increasePostCount(userID);

        return createdRecipe;
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long recipeId) { // Updated variable name
        Optional<Recipe> recipeOpt = recipeService.getRecipeById(recipeId);
        return recipeOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long recipeId, @RequestBody RecipeRequest recipeRequest) { // Updated variable name
        try {
            Recipe recipe = recipeService.updateRecipe(recipeId, recipeRequest);
            return ResponseEntity.ok(recipe);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long recipeId) { // Updated variable name
        Optional<Recipe> recipeOpt = recipeService.getRecipeById(recipeId);
        if (recipeOpt.isPresent()) {
            String userID = "currentUserID";    // 레시피 삭제 후 count 감소
            gradeService.decreaseCommentCount(userID);

            recipeService.deleteRecipe(recipeId);
            return ResponseEntity.ok("삭제 되었습니다");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{recipeId}/confirm")
    public ResponseEntity<String> confirmDeleteRecipe(@PathVariable Long recipeId) { // Updated variable name
        Optional<Recipe> recipeOpt = recipeService.getRecipeById(recipeId);
        if (recipeOpt.isPresent()) {
            return ResponseEntity.ok("정말 삭제하시겠습니까?");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}