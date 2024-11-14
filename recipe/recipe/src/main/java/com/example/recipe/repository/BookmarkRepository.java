package com.example.recipe.repository;

import com.example.recipe.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findAllByUserUserID(String userID);
    //void deleteByUserUserIdAndRecipeRecipeId(String userId, Long recipeId);
    Optional<Bookmark> findByUserUserIDAndRecipeRecipeId(String userID, Long recipeId);
}