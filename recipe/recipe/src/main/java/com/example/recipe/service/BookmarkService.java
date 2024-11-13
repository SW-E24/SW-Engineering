package com.example.recipe.service;

import com.example.recipe.entity.Bookmark;
import com.example.recipe.entity.Member;
import com.example.recipe.entity.Recipe;
import com.example.recipe.repository.BookmarkRepository;
import com.example.recipe.ResourceNotFoundException;
import com.example.recipe.repository.RecipeRepository;
import com.example.recipe.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookmarkService {
    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    public Bookmark addBookmark(Bookmark bookmark) {
        // 사용자와 레시피를 데이터베이스에서 조회
        Member user = memberRepository.findById(bookmark.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + bookmark.getUser().getUserId()));

        Recipe recipe = recipeRepository.findById(bookmark.getRecipe().getRecipeId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + bookmark.getRecipe().getRecipeId()));

        // 조회된 엔티티로 설정
        bookmark.setUser(user);
        bookmark.setRecipe(recipe);
        bookmark.setDate(LocalDateTime.now());

        return bookmarkRepository.save(bookmark);
    }

//    public Bookmark addBookmark(Bookmark bookmark) { //북마크 추가 메소드
//        bookmark.setDate(LocalDateTime.now());
//        return bookmarkRepository.save(bookmark);
//    }

    public List<Bookmark> getBookmarksByUserId(String userId) { //모든 북마크를 가져오는 메소드
        return bookmarkRepository.findAllByUserUserId(userId);
    }

    public void removeBookmark(String userId, Long recipeId) { // 북마크 취소 메소드 추가
        Bookmark bookmark = bookmarkRepository.findByUserUserIdAndRecipeRecipeId(userId, recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found for userId: " + userId + " and recipeId: " + recipeId));

        bookmarkRepository.delete(bookmark);
    }
}
