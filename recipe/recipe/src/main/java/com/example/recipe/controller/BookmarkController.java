package com.example.recipe.controller;

import com.example.recipe.ResourceNotFoundException;
import com.example.recipe.entity.Bookmark;
import com.example.recipe.entity.Member;
import com.example.recipe.repository.BookmarkRepository;
import com.example.recipe.service.BookmarkService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    @Autowired
    private BookmarkService bookmarkService;
    @Autowired
    private BookmarkRepository bookmarkRepository;

    @PostMapping
    public ResponseEntity<Bookmark> addBookmark(@RequestBody Bookmark bookmark) {
        Bookmark newBookmark = bookmarkService.addBookmark(bookmark);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBookmark);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Bookmark>> getBookmarks(@PathVariable String userId) {
        List<Bookmark> bookmarks = bookmarkService.getBookmarksByUserID(userId);
        return ResponseEntity.ok(bookmarks);
    }

    /*
    @DeleteMapping("/{userId}/{recipeId}")
    public ResponseEntity<Void> removeBookmark(@PathVariable String userId, @PathVariable Long recipeId) {
        bookmarkService.removeBookmark(userId, recipeId);  // 북마크 취소 메소드 추가
        return ResponseEntity.noContent().build();
    }
    */

    // 북마크 객체를 받아와서 삭제
    @DeleteMapping
    public ResponseEntity<Void> removeBookmark(@RequestBody Bookmark bookmark) {
        try {
            // 북마크 삭제
            bookmarkService.removeBookmark(bookmark.getUser().getUserId(), bookmark.getRecipe().getRecipeId());
            return ResponseEntity.noContent().build();  // 삭제 성공 응답
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 북마크를 찾을 수 없는 경우
        }
    }
    //현재 로그인한 사용자의 북마크 목록
    @GetMapping("/my-bookmarks")
    public ResponseEntity<List<Bookmark>> getMyBookmarks(HttpSession session) {
        Member currentUser = (Member) session.getAttribute("currentUser");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<Bookmark> bookmarks = bookmarkService.getBookmarksByUserID(currentUser.getUserId());
        return ResponseEntity.ok(bookmarks); // Bookmark 엔티티를 그대로 반환
    }
    //페이지네이션
    @GetMapping("/my-bookmarks-paged")
    public ResponseEntity<Page<Bookmark>> getMyBookmarksPaged(
            HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Member currentUser = (Member) session.getAttribute("currentUser");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인되지 않은 경우
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Bookmark> bookmarks = bookmarkService.getBookmarksByUserIdWithPaging(currentUser.getUserId(), pageable);
        return ResponseEntity.ok(bookmarks);
    }
    //현재 사용자의 북마크 상황확인
    @GetMapping("/is-bookmarked")
    public ResponseEntity<Boolean> isBookmarked(
            @RequestParam String userId,
            @RequestParam Long recipeId) {

        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserUserIdAndRecipeRecipeId(userId, recipeId);
        return ResponseEntity.ok(existingBookmark.isPresent());
    }

}
