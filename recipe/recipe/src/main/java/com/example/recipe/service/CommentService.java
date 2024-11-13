package com.example.recipe.service;

import com.example.recipe.entity.Comment;
import com.example.recipe.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Comment> getCommentsByRecipeId(Long recipeId) {
        // 레시피 ID로 댓글 조회
        return commentRepository.findByRecipeId(recipeId);
    }
}
