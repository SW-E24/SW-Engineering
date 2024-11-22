package com.example.recipe.controller;

import com.example.recipe.dto.CommentRequest;
import com.example.recipe.dto.CommentResponse;
import com.example.recipe.entity.Comment;
import com.example.recipe.entity.Member;
import com.example.recipe.service.CommentService;
import com.example.recipe.service.GradeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private GradeService gradeService;

    // 댓글 작성
    @PostMapping("/create")
    public Comment addComment(@RequestBody CommentRequest commentRequest, HttpSession session) {
        // 댓글 작성한 사용자 저장
        Long recipeId = commentRequest.getRecipeId();
        if (recipeId == null) {
            throw new IllegalArgumentException("recipeId는 필수 값입니다.");
        }
        Member currentUser = (Member) session.getAttribute("currentUser");
        if (currentUser == null) {
            throw new RuntimeException("로그인 상태가 아닙니다.");
        }
        Comment createdComment = commentService.addComment(
                commentRequest.getRecipeId(),
                currentUser.getUserId(),
                commentRequest.getContent()
        );

        gradeService.increaseCommentCount(currentUser.getUserId());
        gradeService.updateMemberGreade(currentUser.getUserId());

        return createdComment;
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
            gradeService.decreaseCommentCount(userId);
            gradeService.updateMemberGreade(userId);
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("댓글을 삭제할 수 없습니다.");
        }
    }

    // 특정 레시피의 댓글 목록 조회
    @GetMapping
    public List<CommentResponse> getCommentsByRecipeId(@RequestParam Long recipeId) {
        return commentService.getCommentsByRecipeId(recipeId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getUserComments(@PathVariable String userId) {
        List<Comment> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }

    /*********
    * jiyeon
    * ********/
    // 로그인한 사용자가 작성한 댓글을 페이지네이션으로 가져오기
    @GetMapping("/my-comments-paged")
    public ResponseEntity<Page<Comment>> getUserCommentsPaged(
            HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Member currentUser = (Member) session.getAttribute("currentUser");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인되지 않은 경우
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentService.getCommentsByUserIdPaged(currentUser.getUserId(), pageable);
        return ResponseEntity.ok(comments);
    }
}
