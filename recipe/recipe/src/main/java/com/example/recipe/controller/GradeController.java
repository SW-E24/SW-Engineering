package com.example.recipe.controller;

import com.example.recipe.entity.Grade;
import com.example.recipe.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    // 게시글 카운트 증가
    @PostMapping("/{userID}/post/increase")
    public ResponseEntity<String> increasePostCount(@PathVariable String userID) {
        gradeService.increasePostCount(userID);
        gradeService.updateMemberGreade(userID); // 등급 업데이트
        return ResponseEntity.ok("게시글 카운트가 증가했습니다.");
    }

    // 게시글 카운트 감소
    @PostMapping("/{userID}/post/decrease")
    public ResponseEntity<String> decreasePostCount(@PathVariable String userID) {
        try {
            gradeService.decreasePostCount(userID);
            gradeService.updateMemberGreade(userID); // 등급 업데이트
            return ResponseEntity.ok("게시글 카운트가 감소했습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 댓글 카운트 증가
    @PostMapping("/{userID}/comment/increase")
    public ResponseEntity<String> increaseCommentCount(@PathVariable String userID) {
        gradeService.increaseCommentCount(userID);
        gradeService.updateMemberGreade(userID); // 등급 업데이트
        return ResponseEntity.ok("댓글 카운트가 증가했습니다.");
    }

    // 댓글 카운트 감소
    @PostMapping("/{userID}/comment/decrease")
    public ResponseEntity<String> decreaseCommentCount(@PathVariable String userID) {
        try {
            gradeService.decreaseCommentCount(userID);
            gradeService.updateMemberGreade(userID); // 등급 업데이트
            return ResponseEntity.ok("댓글 카운트가 감소했습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 현재 회원의 등급 조회
    @GetMapping("/{userID}")
    public ResponseEntity<Grade> getMemberGrade(@PathVariable String userID) {
        Optional<Grade> grade = gradeService.findByUserID(userID);
        if (grade.isPresent()) {
            return ResponseEntity.ok(grade.get());
        } else {
            return ResponseEntity.notFound().build();   // 등급 없으면 404
        }
    }
}
