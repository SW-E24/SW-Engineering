package com.example.recipe.service;

import com.example.recipe.dto.RecipeRequest;
import com.example.recipe.entity.Member;
import com.example.recipe.entity.Recipe;
import com.example.recipe.repository.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // 현재 로그인한 사용자 정보를 받아서 사용하기 위해 수정
    public Recipe createRecipe(Member user, String title, String category, List<Recipe.Ingredient> ingredients, List<Recipe.Step> steps, String description) {
        Recipe recipe = new Recipe(title, category, LocalDateTime.now(), ingredients, steps, description);
        recipe.setUser(user);   // 작성자 설정
        return recipeRepository.save(recipe);
    }
  
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(Long recipeId, RecipeRequest updatedRecipe) {
        Optional<Recipe> existingRecipeOpt = recipeRepository.findById(recipeId);
        if (existingRecipeOpt.isPresent()) {
            Recipe existingRecipe = existingRecipeOpt.get();

            // 제목, 카테고리 등 업데이트
            existingRecipe.setTitle(updatedRecipe.getTitle());
            existingRecipe.setCategory(updatedRecipe.getCategory());
            existingRecipe.setIngredients(updatedRecipe.getIngredients());
            existingRecipe.setSteps(updatedRecipe.getSteps());
            existingRecipe.setDescription(updatedRecipe.getDescription());

            return recipeRepository.save(existingRecipe);
        } else {
            throw new RuntimeException("Recipe not found");
        }
    }


    public Optional<Recipe> getRecipeById(Long recipeId) {
        return recipeRepository.findById(Long.valueOf(String.valueOf(recipeId)));
    }

    public List<Recipe> getRecipesByUserId(String userId) { // 특정 사용자의 레시피만 가져오는 메서드
        return recipeRepository.findAllByUserUserId(userId);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Page<Recipe> getAllRecipe(Pageable pageable) {
        return recipeRepository.findAllRecipe(pageable);
    }

    public void deleteRecipe(Long recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    // jiyeon 페이지네이션
    public Page<Recipe> getRecipesByUserIdPaged(String userId, Pageable pageable) {
        return recipeRepository.findAllByUserUserId(userId, pageable);
    }
}