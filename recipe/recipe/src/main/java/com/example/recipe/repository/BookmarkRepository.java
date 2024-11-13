package com.example.recipe.repository;

import com.example.recipe.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findAllByUserUserId(String userId);
    //void deleteByUserUserIdAndRecipeRecipeId(String userId, Long recipeId);
    Optional<Bookmark> findByUserUserIdAndRecipeRecipeId(String userId, Long recipeId);
}