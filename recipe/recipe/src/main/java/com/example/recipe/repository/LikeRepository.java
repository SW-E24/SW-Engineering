package com.example.recipe.repository;

import com.example.recipe.entity.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserUserIdAndRecipeRecipeId(String userId, Long recipeId);
    Optional<Like> findByUserUserIdAndRecipeRecipeId(String userId, Long recipeId);
    //Optional<Like> findByUserAndRecipe(User user, Recipe recipe);
    List<Like> findAllByUserUserId(String userId);

    // 페이지네이션 위해 추가
    Page<Like> findAllByUserUserId(String userId, Pageable pageable);
}
