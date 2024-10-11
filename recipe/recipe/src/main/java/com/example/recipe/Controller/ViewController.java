package com.example.recipe.Controller;

import com.example.recipe.Service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/views")
public class ViewController {
    @Autowired
    private ViewService viewService;

    @PostMapping("/{recipeId}")
    public ResponseEntity<String> incrementViewCount(@PathVariable Long recipeId) {
        viewService.incrementViewCount(recipeId);
        return ResponseEntity.ok("조회수가 증가했습니다.");
    }
}