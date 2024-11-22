package com.example.recipe.repository;

import com.example.recipe.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    //    Object findAllByUserUserID(String userID);
    List<Recipe> findAllByUserUserId(String userId);
    Page<Recipe> findAllByUserUserId(String userId, Pageable pageable);     // jiyeon 페이지네이션 추가

    // 제목으로 레시피 찾기
    Page<Recipe> findByTitleContaining(String keyword, Pageable pageable);

    // 재료 이름으로 레시피 찾기
    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE i.name LIKE %:ingredient%")
    Page<Recipe> findByIngredientContaining(@Param("ingredient") String ingredient, Pageable pageable);

    // 카테고리별 레시피 찾기
    Page<Recipe> findByCategory(String category, Pageable pageable);

    // 모든 레시피 들고오기
    @Query("SELECT r FROM Recipe r")
    Page<Recipe> findAllRecipe(Pageable pageable);
}