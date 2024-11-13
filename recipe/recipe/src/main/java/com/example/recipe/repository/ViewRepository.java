package com.example.recipe.repository;

import com.example.recipe.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViewRepository extends JpaRepository<View, Long> {
    Optional<View> findByRecipeRecipeId(Long recipeId);
}