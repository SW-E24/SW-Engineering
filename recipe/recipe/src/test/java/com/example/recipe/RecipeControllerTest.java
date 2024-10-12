package com.example.recipe;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.recipe.Entity.Recipe;
import com.example.recipe.Service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @Test
    public void testCreateRecipe() throws Exception {
        // Given
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1L);
        recipe.setTitle("New Recipe");
        recipe.setDescription("Description of new recipe");

        // When
        when(recipeService.createRecipe(any(Recipe.class))).thenReturn(recipe);

        // Then
        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Recipe\",\"description\":\"Description of new recipe\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Recipe"))
                .andExpect(jsonPath("$.description").value("Description of new recipe"));
    }

    @Test
    public void testGetAllRecipes() throws Exception {
        // Given
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1L);
        recipe.setTitle("Test Recipe");

        List<Recipe> recipes = Arrays.asList(recipe);

        // When
        when(recipeService.getAllRecipes()).thenReturn(recipes);

        // Then
        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Recipe"));
    }
}
