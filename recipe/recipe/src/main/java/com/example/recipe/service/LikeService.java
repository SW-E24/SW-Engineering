package com.example.recipe.service;

import com.example.recipe.entity.Bookmark;
import com.example.recipe.entity.Like;
import com.example.recipe.entity.Member;
import com.example.recipe.entity.Recipe;
import com.example.recipe.repository.LikeRepository;
import com.example.recipe.repository.RecipeRepository;
import com.example.recipe.repository.MemberRepository;
import com.example.recipe.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    public void addLike(String userId, Long recipeId) {
        Member user = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Like like = new Like();
        like.setUser(user);
        like.setRecipe(recipe);
        likeRepository.save(like);
    }

    public void removeLike(String userID, Long recipeId) {
        Like like = likeRepository.findByUserUserIdAndRecipeRecipeId(userID, recipeId)
                .orElseThrow(() -> new RuntimeException("Like not found"));
        likeRepository.delete(like);
    }

    public boolean checkLikeExists(String userID, Long recipeId) { //좋아요를 눌렀는지 확이하는 메소드
        return likeRepository.existsByUserUserIdAndRecipeRecipeId(userID, recipeId);
    }

    public List<Like> getLikesByUserId(String userId) {
//        Member user = memberRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
//        return likeRepository.findAllByUserUserId(userId);
        return likeRepository.findAllByUserUserId(userId);
    }

    // 페이지네이션 추가
    public Page<Like> getLikesByUserIdWithPaging(String userId, Pageable pageable) {
        return likeRepository.findAllByUserUserId(userId, pageable);
    }
}
