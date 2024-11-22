package com.example.recipe.controller;

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

    // 메소드 관리 방법 1 - toggleLike 으로 add+remove 기능
    // html 에서 버튼 토글 구현 시 param 형태로 서버에 전송해야 한다. - 구현 필요
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> toggleLike(@RequestParam String userId, @RequestParam Long recipeId) {
        // 이미 좋아요가 눌러져있는지 확인
        boolean existsLike = likeRepository.existsByUserUserIdAndRecipeRecipeId(userId, recipeId);
        if(!existsLike) {   // 좋아요가 안 눌러져있으면 추가
            likeService.addLike(userId, recipeId);
        } else {    // 좋아요가 눌러져있으면 버튼을 다시 누를 때 삭제하기
            likeService.removeLike(userId, recipeId);
        }
        return ResponseEntity.noContent().build();
    }

    // 메소드 관리 방법 2 - add와 remove를 따로, id파라미터를 받기
    // 구현해보면 대충 아래 방법이겠지..? post view.html 도 id파라미터 전달하는걸로 스크립트 짜야함
    /*
    @PostMapping("/addLike")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addLike(@RequestParam String userId, @RequestParam Long recipeId) {
        // 좋아요 상태 확인
        boolean existsLike = likeRepository.existsByUserUserIdAndRecipeRecipeId(userId, recipeId);
        if (existsLike) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 이미 좋아요가 눌러진 상태
        }
        likeService.addLike(userId, recipeId); // 좋아요 추가

        // 로그 추가
        System.out.println("Saving like for user: " + userId + " and recipe: " + recipeId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/removeLike")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removeLike(@RequestParam String userId, @RequestParam Long recipeId) {
        // 좋아요 상태 확인
        boolean existsLike = likeRepository.existsByUserUserIdAndRecipeRecipeId(userId, recipeId);
        if (!existsLike) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 좋아요가 눌려있지 않은 상태
        }
        likeService.removeLike(userId, recipeId); // 좋아요 삭제
        return ResponseEntity.noContent().build();
    }
     */

    // 메소드 관리 방법 3 - add와 remove를 따로, 객체를 전달 (jiyeon 구조 참고)
    // html 에서 버튼 토글 구현 시 json 형태로 서버에 전송해야 한다 (jiyeon 구조 활용하면 됨)
    // 메소드는 객체 받는걸로 새로 작성해야됨

    /*
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@RequestParam String userId, @RequestParam Long recipeId) {
        likeService.removeLike(userId, recipeId);
    }
    */

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkLikeExists(@RequestParam String userId, @RequestParam Long recipeId) {
        boolean exists = likeService.checkLikeExists(userId, recipeId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Like>> getLikesByUserId(@PathVariable String userId) {
        List<Like> likes = likeService.getLikesByUserId(userId);
        return ResponseEntity.ok(likes);
    }

    /*******************************
    * jiyeon 구조를 참고하여 좋아요도 구현
     * - 현재 사용자의 좋아요 유무 확인
     * - 현재 사용자의 좋아요 목록 반환
     * - 페이지네이션
    * ******************************/
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

