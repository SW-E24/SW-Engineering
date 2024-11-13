package com.example.recipe.service;

import com.example.recipe.dto.RecipeRequest;
import com.example.recipe.entity.Recipe;
import com.example.recipe.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe createRecipe(String title, String category, List<Recipe.Ingredient> ingredients, List<Recipe.Step> steps, String description) {
        Recipe recipe = new Recipe(title, category, LocalDateTime.now(), ingredients, steps, description);
        return recipeRepository.save(recipe);
    }
  
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }
    public Recipe updateRecipe(Long recipeId, RecipeRequest updatedRecipe) {
        Optional<Recipe> existingRecipeOpt = recipeRepository.findById(Long.valueOf(String.valueOf(recipeId)));
        if (existingRecipeOpt.isPresent()) {
            Recipe existingRecipe = existingRecipeOpt.get();
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

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public void deleteRecipe(Long recipeId) {
        recipeRepository.deleteById(Long.valueOf(String.valueOf(recipeId)));
    }
}
