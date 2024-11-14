package com.example.recipe.controller;

import com.example.recipe.entity.Bookmark;
import com.example.recipe.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    @Autowired
    private BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<Bookmark> addBookmark(@RequestBody Bookmark bookmark) {
        Bookmark newBookmark = bookmarkService.addBookmark(bookmark);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBookmark);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Bookmark>> getBookmarks(@PathVariable String userID) {
        List<Bookmark> bookmarks = bookmarkService.getBookmarksByUserID(userID);
        return ResponseEntity.ok(bookmarks);
    }

    @DeleteMapping("/{userId}/{recipeId}")
    public ResponseEntity<Void> removeBookmark(@PathVariable String userID, @PathVariable Long recipeId) {
        bookmarkService.removeBookmark(userID, recipeId);  // 북마크 취소 메소드 추가
        return ResponseEntity.noContent().build();
    }
}
