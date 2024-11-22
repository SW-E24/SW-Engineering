package com.example.recipe.service;

import com.example.recipe.entity.Comment;
import com.example.recipe.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 추가 테스트")
    void testAddComment() {
        Long recipeId = Long.valueOf(String.valueOf(1L));
        String userId = String.valueOf(1L);
        String content = "테스트 댓글입니다.";

        Comment savedComment = commentService.addComment(recipeId, String.valueOf(userId), content);

        Optional<Comment> optionalComment = commentRepository.findById(savedComment.getId());
        assertThat(optionalComment).isPresent();
        assertThat(optionalComment.get().getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void testEditComment() {
        Long recipeId = Long.valueOf(String.valueOf(1L));
        String userId = String.valueOf(1L);
        String originalContent = "원래 댓글";
        Comment savedComment = commentService.addComment(Long.valueOf(String.valueOf(recipeId)), String.valueOf(userId), originalContent);

        String updatedContent = "수정된 댓글";
        Comment updatedComment = commentService.editComment(savedComment.getId(), userId, updatedContent);

        assertThat(updatedComment.getContent()).isEqualTo(updatedContent);
    }

    @Test
    @DisplayName("댓글 수정 권한 오류 테스트")
    void testEditCommentUnauthorized() {
        Long recipeId = 1L;
        String userId = String.valueOf(1L);
        String originalContent = "원래 댓글";
        Comment savedComment = commentService.addComment(recipeId, userId, originalContent);

        String otherUserId = String.valueOf(2L);
        String updatedContent = "수정된 댓글";

        assertThrows(IllegalArgumentException.class, () ->
                commentService.editComment(savedComment.getId(), otherUserId, updatedContent)
        );
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void testDeleteComment() {
        Long recipeId = 1L;
        String userId = String.valueOf(1L);
        String content = "삭제할 댓글";
        Comment savedComment = commentService.addComment(recipeId, userId, content);

        commentService.deleteComment(savedComment.getId(), userId);

        Optional<Comment> optionalComment = commentRepository.findById(savedComment.getId());
        assertThat(optionalComment).isNotPresent();
    }

    @Test
    @DisplayName("댓글 삭제 권한 오류 테스트")
    void testDeleteCommentUnauthorized() {
        Long recipeId = 1L;
        String userId = String.valueOf(1L);
        String content = "삭제할 댓글";
        Comment savedComment = commentService.addComment(recipeId, userId, content);

        String otherUserId = "2L";

        assertThrows(IllegalArgumentException.class, () ->
                commentService.deleteComment(savedComment.getId(), otherUserId)
        );
    }

    @BeforeEach
    void setUp() {
        // 각 테스트 전에 댓글 삭제
        commentRepository.deleteAll();
    }

//    @Test
//    @DisplayName("레시피 ID로 댓글 조회 테스트")
//    void testGetCommentsByRecipeId() {
//        Long recipeId = 1L;
//        String userId = String.valueOf(1L);
//        commentService.addComment(recipeId, userId, "댓글 1");
//        commentService.addComment(recipeId, userId, "댓글 2");
//
//        List<Comment> comments = commentService.getCommentsByRecipeId(recipeId);
//
//        assertThat(comments).hasSize(2);
//        assertThat(comments.get(0).getRecipeId()).isEqualTo(recipeId);
//    }
}
