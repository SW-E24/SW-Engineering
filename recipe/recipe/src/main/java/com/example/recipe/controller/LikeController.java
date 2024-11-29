package com.example.recipe.controller;

import com.example.recipe.ResourceNotFoundException;
import com.example.recipe.entity.Like;
import com.example.recipe.entity.Member;
import com.example.recipe.repository.LikeRepository;
import com.example.recipe.service.LikeService;
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
@RequestMapping("/api/likes")
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    LikeRepository likeRepository;

    @PostMapping
    public ResponseEntity<Like> addLike(@RequestBody Like like) {
        Like newLike = likeService.addLike(like);
        return ResponseEntity.status(HttpStatus.CREATED).body(newLike);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Like>> getLikes(@PathVariable String userId) {
        List<Like> likes = likeService.getLikesByUserId(userId);
        return ResponseEntity.ok(likes);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeLike(@RequestBody Like like) {
        try {
            likeService.removeLike(like.getUser().getUserId(), like.getRecipe().getRecipeId());
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 현재 사용자가 보고있는 글의 좋아요 유무 확인 - checkLikeExists 를 대체
    @GetMapping("/is-liked")
    public ResponseEntity<Boolean> isLiked(
            @RequestParam String userId,
            @RequestParam Long recipeId) {

        boolean exists = likeRepository.existsByUserUserIdAndRecipeRecipeId(userId, recipeId);
        return ResponseEntity.ok(exists);
    }

    // 현재 로그인한 사용자의 좋아요 목록 - getLikesByUserId 를 대체
    @GetMapping("/my-likes")
    public ResponseEntity<List<Like>> getMyLikes(HttpSession session) {
        Member currentUser = (Member) session.getAttribute("currentUser");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<Like> likes = likeService.getLikesByUserId(currentUser.getUserId());
        return ResponseEntity.ok(likes);
    }

    // 페이지네이션을 위해 페이지 형태로 목록 반환
    @GetMapping("/my-likes-paged")
    public ResponseEntity<?> getMyLikesPaged(
            HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Member currentUser = (Member) session.getAttribute("currentUser");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Like> likes = likeService.getLikesByUserIdWithPaging(currentUser.getUserId(), pageable);
        return ResponseEntity.ok(likes);
    }
}

