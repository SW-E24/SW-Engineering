package com.example.recipe;

import com.example.recipe.dto.RecipeRequest;
import com.example.recipe.entity.Recipe;
import com.example.recipe.repository.RecipeRepository;
import com.example.recipe.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RecipeServiceTests {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();
    }

    @Test
    public void testCreateRecipe() {
        // Given
        Recipe.Ingredient ingredient = new Recipe.Ingredient("Sugar", "2 cups");
        Recipe.Step step = new Recipe.Step("Mix ingredients", null);

        RecipeRequest recipeRequest = new RecipeRequest(
                "Test Recipe",
                "양식",
                Arrays.asList(ingredient),
                Arrays.asList(step),
                "Test Description"
        );

        // When
        Recipe savedRecipe = recipeService.createRecipe(
                recipeRequest.getTitle(),
                recipeRequest.getCategory(),
                recipeRequest.getIngredients(),
                recipeRequest.getSteps(),
                recipeRequest.getDescription()
        );

        // Then
        assertNotNull(savedRecipe);
        assertNotNull(savedRecipe.getRecipeId());
        assertEquals("Test Recipe", savedRecipe.getTitle());
        assertEquals("양식", savedRecipe.getCategory());
        assertEquals(1, savedRecipe.getIngredients().size());
        assertEquals(1, savedRecipe.getSteps().size());
        assertEquals("Test Description", savedRecipe.getDescription());

        // Verify it was saved in the database
        Optional<Recipe> foundRecipe = recipeRepository.findById(savedRecipe.getRecipeId());
        assertTrue(foundRecipe.isPresent());
        assertEquals("Test Recipe", foundRecipe.get().getTitle());
    }

    @Test
    public void testUpdateRecipe() {
        // Given - Create initial recipe
        Recipe.Ingredient initialIngredient = new Recipe.Ingredient("Flour", "1 cup");
        Recipe.Step initialStep = new Recipe.Step("Initial step", null);

        Recipe initialRecipe = new Recipe(
                "Initial Recipe",
                "양식",
                LocalDateTime.now(),
                Arrays.asList(initialIngredient),
                Arrays.asList(initialStep),
                "Initial Description"
        );

        Recipe savedRecipe = recipeRepository.save(initialRecipe);

        // Create update request
        Recipe.Ingredient updatedIngredient = new Recipe.Ingredient("Sugar", "2 cups");
        Recipe.Step updatedStep = new Recipe.Step("Updated step", null);

        RecipeRequest updateRequest = new RecipeRequest(
                "Updated Recipe",
                "양식",
                Arrays.asList(updatedIngredient),
                Arrays.asList(updatedStep),
                "Updated Description"
        );

        // When
        Recipe updatedRecipe = recipeService.updateRecipe(savedRecipe.getRecipeId(), updateRequest);

        // Then
        assertNotNull(updatedRecipe);
        assertEquals("Updated Recipe", updatedRecipe.getTitle());
        assertEquals("Updated Description", updatedRecipe.getDescription());
        assertEquals(1, updatedRecipe.getIngredients().size());
        assertEquals("Sugar", updatedRecipe.getIngredients().get(0).getName());
        assertEquals(1, updatedRecipe.getSteps().size());
        assertEquals("Updated step", updatedRecipe.getSteps().get(0).getDescription());
    }

    @Test
    public void testDeleteRecipe() {
        Recipe recipe = new Recipe("Title", "양식", LocalDateTime.now(),
                Arrays.asList(new Recipe.Ingredient("Salt", "1 tsp")),
                Arrays.asList(new Recipe.Step("Add salt", null)),
                "Description");

        // Save the recipe and then delete it
        Recipe savedRecipe = recipeRepository.save(recipe);
        Long recipeId = savedRecipe.getRecipeId();

        recipeService.deleteRecipe(recipeId);

        Optional<Recipe> deletedRecipe = recipeRepository.findById(recipeId);
        assertFalse(deletedRecipe.isPresent());
    }

    @Test
    public void testGetRecipeById() {
        Recipe recipe = new Recipe("Title", "양식", LocalDateTime.now(),
                Arrays.asList(new Recipe.Ingredient("Sugar", "2 cups")),
                Arrays.asList(new Recipe.Step("Step description", null)),
                "Description");

        // Save the recipe
        Recipe savedRecipe = recipeRepository.save(recipe);
        Long recipeId = savedRecipe.getRecipeId();

        Optional<Recipe> foundRecipe = recipeService.getRecipeById(recipeId);

        assertTrue(foundRecipe.isPresent());
        assertEquals("Title", foundRecipe.get().getTitle());
    }

    @Test
    public void testGetAllRecipes() {
        Recipe recipe1 = new Recipe("Title 1", "한식", LocalDateTime.now(),
                Arrays.asList(new Recipe.Ingredient("Rice", "1 bowl")),
                Arrays.asList(new Recipe.Step("Cook rice", null)),
                "Description 1");

        Recipe recipe2 = new Recipe("Title 2", "양식", LocalDateTime.now(),
                Arrays.asList(new Recipe.Ingredient("Pasta", "200g")),
                Arrays.asList(new Recipe.Step("Boil pasta", null)),
                "Description 2");

        // Save both recipes
        recipeRepository.save(recipe1);
        recipeRepository.save(recipe2);

        List<Recipe> allRecipes = recipeService.getAllRecipes();

        assertEquals(2, allRecipes.size());
        assertEquals("Title 1", allRecipes.get(0).getTitle());
        assertEquals("Title 2", allRecipes.get(1).getTitle());
    }
}
