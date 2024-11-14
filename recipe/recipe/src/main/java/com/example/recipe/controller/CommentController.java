package com.example.recipe.controller;

import com.example.recipe.dto.CommentRequest;
import com.example.recipe.entity.Comment;
import com.example.recipe.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    // 댓글 작성
    @PostMapping
    public Comment addComment(@RequestBody CommentRequest commentRequest) {
        return commentService.addComment(
                commentRequest.getRecipeId(),
                commentRequest.getUserId(),
                commentRequest.getContent()
        );
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> editComment(
            @PathVariable(name = "commentId") Long commentId,
            @RequestBody CommentRequest commentRequest
    ) {
        try {
            Comment comment = commentService.editComment(
                    commentId,
                    commentRequest.getUserId(),
                    commentRequest.getContent()
            );
            return ResponseEntity.ok(comment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable(name = "commentId") Long commentId,
            @RequestParam("userId")  String userId
    ) {
        try {
            commentService.deleteComment(commentId, userId);
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("댓글을 삭제할 수 없습니다.");
        }
    }

    // 특정 레시피의 댓글 목록 조회
    @GetMapping
    public List<Comment> getCommentsByRecipeId(@RequestParam Long recipeId) {
        return commentService.getCommentsByRecipeId(recipeId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getUserComments(@PathVariable String userId) {
        List<Comment> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }
}
