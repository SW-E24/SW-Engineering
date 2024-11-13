package com.example.recipe.controller;

import com.example.recipe.entity.Like;
import com.example.recipe.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addLike(@RequestParam String userId, @RequestParam Long recipeId) {
        likeService.addLike(userId, recipeId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@RequestParam String userId, @RequestParam Long recipeId) {
        likeService.removeLike(userId, recipeId);
    }

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
}

