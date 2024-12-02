package com.example.recipe.repository;

import com.example.recipe.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByUserUserIdAndRecipeRecipeId(String userId, Long recipeId);
    List<Bookmark> findAllByUserUserId(String userId);
    //void deleteByUserUserIdAndRecipeRecipeId(String userId, Long recipeId);
    Optional<Bookmark> findByUserUserIdAndRecipeRecipeId(String userID, Long recipeId);
    Page<Bookmark> findAllByUserUserId(String userId, Pageable pageable);//페이지네이션 적용 메서드
}