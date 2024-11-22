package com.example.recipe.service;

import com.example.recipe.dto.CommentResponse;
import com.example.recipe.entity.Comment;
import com.example.recipe.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment addComment(Long recipeId, String userId, String content) {
        Comment comment = new Comment(recipeId, userId, content);
        return commentRepository.save(comment);
    }

    public Comment editComment(Long id, String userId, String content) {
        Optional<Comment> optionalComment = commentRepository.findById(Long.valueOf(id));

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            if (comment.getUserId().equals(userId)) {
                comment.setContent(content);
                return commentRepository.save(comment);
            }
            else {
                throw new IllegalArgumentException("사용자 권한이 없습니다.");
            }
        }
        else {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다.");
        }
    }

    public void deleteComment(Long commentId, String userId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            if (comment.getUserId().equals(userId)) {
                commentRepository.delete(comment);
            } else {
                throw new IllegalArgumentException("사용자 권한이 없습니다.");
            }
        } else {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다.");
        }
    }

    public List<CommentResponse> getCommentsByRecipeId(Long recipeId) {
        return commentRepository.findCommentsByRecipeId(recipeId);
    }

    public List<Comment> getCommentsByUserId(String userId) {
        return commentRepository.findAllByUserId(userId);
    }

    // jiyeon
    // 로그인한 사용자가 작성한 댓글을 페이지네이션하여 가져오기
    public Page<Comment> getCommentsByUserIdPaged(String userId, Pageable pageable) {
        return commentRepository.findAllByUserId(userId, pageable);
    }
}
