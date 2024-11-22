package com.example.recipe.controller;

import ch.qos.logback.core.model.Model;
import com.example.recipe.dto.RecipeRequest;
import com.example.recipe.entity.Grade;
import com.example.recipe.entity.Member;
import com.example.recipe.entity.Recipe;
import com.example.recipe.service.GradeService;
import com.example.recipe.service.RecipeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final GradeService gradeService;

    public RecipeController(RecipeService recipeService, GradeService gradeService) {
        this.recipeService = recipeService;
        this.gradeService = gradeService;
    }

    @PostMapping("/create")
    public Recipe createRecipe(@RequestBody RecipeRequest recipeRequest, HttpSession session) {
        if (!recipeRequest.getCategory().matches("양식|한식|중식|일식|디저트")) {
            throw new IllegalArgumentException("카테고리는 양식, 한식, 중식, 일식, 디저트 중 하나여야 합니다.");
        }

        // 레시피 등록 테스트에 작성자의 아이디가 테이블에 저장되지 않는 것을 해결하기 위한 추가 코드
        Member currentUser = (Member) session.getAttribute("currentUser");
        if (currentUser == null) {
            throw new RuntimeException("로그인 상태가 아닙니다.");
        }

        // 게시글 개수 증가를 위해 추가한 코드
        // 레시피를 바로 리턴하지 않고 레시피 객체 생성해서 다룬다
        Recipe createdRecipe = recipeService.createRecipe(
                currentUser,
                recipeRequest.getTitle(),
                recipeRequest.getCategory(),
                recipeRequest.getIngredients(),
                recipeRequest.getSteps(),
                recipeRequest.getDescription()
        );

        // 게시글 개수 증가
        gradeService.increasePostCount(currentUser.getUserId());
        // 게시글 개수 변경 때 마다 회원 등급을 갱신해야함
        gradeService.updateMemberGreade(currentUser.getUserId());

        return createdRecipe;
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long recipeId) {
        Optional<Recipe> recipeOpt = recipeService.getRecipeById(recipeId);
        return recipeOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long recipeId, @RequestBody RecipeRequest recipeRequest) {
        try {
            Recipe recipe = recipeService.updateRecipe(recipeId, recipeRequest);
            return ResponseEntity.ok(recipe);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 게시글 삭제 시 게시글 감소 로직 추가
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long recipeId) {
        Optional<Recipe> recipeOpt = recipeService.getRecipeById(recipeId);
        if (recipeOpt.isPresent()) {
            Recipe recipe = recipeOpt.get();
            Member currentUser = recipe.getUser();  // 레시피 작성자 가져오기

            // 레시피 삭제
            recipeService.deleteRecipe(recipeId);

            // 게시글 수 감소
            gradeService.decreasePostCount(currentUser.getUserId());
            gradeService.updateMemberGreade(currentUser.getUserId());   // 개수 변경 시 업데이트 로직 매번 실행

            return ResponseEntity.ok("삭제 되었습니다");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{recipeId}/confirm")
    public ResponseEntity<String> confirmDeleteRecipe(@PathVariable Long recipeId) {
        Optional<Recipe> recipeOpt = recipeService.getRecipeById(recipeId);
        if (recipeOpt.isPresent()) {
            return ResponseEntity.ok("정말 삭제하시겠습니까?");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recipe>> getRecipesByUserId(@PathVariable String userId) { // 특정 사용자의 레시피 조회
        List<Recipe> recipes = recipeService.getRecipesByUserId(userId);
        return ResponseEntity.ok(recipes);
    }

    /********
    * jiyeon
    * *******/
    @GetMapping("/myposts")
    public ResponseEntity<Page<Recipe>> getMyPosts(
            HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Member currentUser = (Member) session.getAttribute("currentUser");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> myPosts = recipeService.getRecipesByUserIdPaged(currentUser.getUserId(), pageable);
        return ResponseEntity.ok(myPosts);
    }

}