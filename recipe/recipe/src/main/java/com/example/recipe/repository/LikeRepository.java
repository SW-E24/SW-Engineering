package com.example.recipe.repository;

import com.example.recipe.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserUserIDAndRecipeRecipeId(String userID, Long recipeId);
    Optional<Like> findByUserUserIDAndRecipeRecipeId(String userID, Long recipeId);
    //Optional<Like> findByUserAndRecipe(User user, Recipe recipe);
    List<Like> findAllByUserUserID(String userID);
}
