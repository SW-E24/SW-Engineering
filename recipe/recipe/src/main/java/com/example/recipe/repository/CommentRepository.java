package com.example.recipe.repository;

import com.example.recipe.dto.CommentResponse;
import com.example.recipe.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByRecipeId(Long recipeId);
    List<Comment> findAllByUserId(String userId);

    // jiyeon
    // 로그인한 사용자가 작성한 댓글을 페이지네이션하여 가져오는 메서드
    Page<Comment> findAllByUserId(String userId, Pageable pageable);

    // gahyeon
    @Query("SELECT new com.example.recipe.dto.CommentResponse(c.id, c.recipeId, c.userId, m.userName, c.content) " +
            "FROM Comment c " +
            "JOIN Member m ON c.userId = m.userId " +
            "WHERE c.recipeId = :recipeId")
    List<CommentResponse> findCommentsByRecipeId(Long recipeId);

}
