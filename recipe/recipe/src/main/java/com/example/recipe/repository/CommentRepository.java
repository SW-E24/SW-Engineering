package com.example.recipe.repository;

import com.example.recipe.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByRecipeId(Long recipeId);
    List<Comment> findAllByUserId(String userId);
    //Object findAllByUserId(String userId);
}
