package com.example.recipe.repository;

import com.example.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Object findAllByUserUserID(String userID);

    // 검색 기능을 위한 메소드
    List<Recipe> findByTitleContaining(String keyword);

    // 재료 이름으로 레시피 찾기
    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE i.name = :ingredientName")
    List<Recipe> findByIngredientName(@Param("ingredientName") String ingredientName);

    // 카테고리별 레시피 찾기
    List<Recipe> findByCategory(String category);

    // 메뉴 추천을 위해 랜덤한 레시피 가져오기
    @Query(value = "SELECT * FROM Recipe ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Recipe findRandomRecipe();
}