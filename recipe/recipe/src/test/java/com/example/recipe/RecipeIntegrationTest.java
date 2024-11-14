package com.example.recipe;

import com.example.recipe.entity.*;
import com.example.recipe.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RecipeIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private RecipeRepository recipeRepository;
    @MockBean
    private LikeRepository likeRepository;
    @MockBean
    private ViewRepository viewRepository;
    @MockBean
    private BookmarkRepository bookmarkRepository;
    @MockBean
    private CommentRepository commentRepository;

    private Member testUser;
    private Recipe testRecipe;
    private Like testLike;
    private Bookmark testBookmark;
    private Comment testComment;
    private View testView;

    @BeforeEach
    void setUp() {
        testUser = new Member();
        testUser.setUserID("testUser123");
        testUser.setUserEmail("test@example.com");
        testUser.setUserName("Test User");
        testUser.setUserPW("password123");
        testUser.setUserPhone("01012341234");
        //testUser.setUserGrade(new Grade("testUser123", GradeType.ONE, 10, 5));

        testRecipe = new Recipe();
        testRecipe.setRecipeId(1L);
        testRecipe.setTitle("Test Recipe");
        testRecipe.setUser(testUser);
        testRecipe.setDescription("Test Description");
        testRecipe.setDate(LocalDate.from(LocalDateTime.now()));

        testLike = new Like();
        testLike.setUser(testUser);
        testLike.setRecipe(testRecipe);

        testBookmark = new Bookmark();
        testBookmark.setUser(testUser);
        testBookmark.setRecipe(testRecipe);
        testBookmark.setDate(LocalDateTime.now());

        testComment = new Comment();
        testComment.setUserId(testUser.getUserID());
        testComment.setRecipeId(testRecipe.getRecipeId());
        testComment.setContent("Test Comment");

        testView = new View();
        testView.setRecipe(testRecipe);
        testView.setCountView(1);

        setupMockRepositories();
    }

    private void setupMockRepositories() {
        Mockito.when(memberRepository.findById(testUser.getUserID())).thenReturn(Optional.of(testUser));
        Mockito.when(recipeRepository.findById(testRecipe.getRecipeId())).thenReturn(Optional.of(testRecipe));
        Mockito.when(likeRepository.save(any(Like.class))).thenReturn(testLike);
        Mockito.when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(testBookmark);
        Mockito.when(viewRepository.save(any(View.class))).thenReturn(testView);
        Mockito.when(memberRepository.save(any(Member.class))).thenReturn(testUser);
        Mockito.when(recipeRepository.findAllByUserUserID(testUser.getUserID())).thenReturn(Arrays.asList(testRecipe));
        Mockito.when(commentRepository.findAllByUserId(testUser.getUserID())).thenReturn(Arrays.asList(testComment));
        Mockito.when(bookmarkRepository.findAllByUserUserID(testUser.getUserID())).thenReturn(Arrays.asList(testBookmark));
    }

    @Test
    void testUploadProfilePicture() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "profile.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        mockMvc.perform(multipart("/api/users/{userID}/profile-picture", testUser.getUserID())
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("프로필 사진이 성공적으로 업데이트되었습니다."));
    }

    @Test
    void testGetUserInfo() throws Exception {
        mockMvc.perform(get("/api/users/{userID}", testUser.getUserID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(testUser.getUserID()))
                .andExpect(jsonPath("$.email").value(testUser.getUserEmail()))
                .andExpect(jsonPath("$.userName").value(testUser.getUserName()));
    }

    @Test
    void testUpdateUserInfo() throws Exception {
        Member updatedUser = new Member();
        updatedUser.setUserName("Updated Name");
        updatedUser.setUserEmail("updated@example.com");

        mockMvc.perform(put("/api/users/{userID}", testUser.getUserID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void testAddLike() throws Exception {
        mockMvc.perform(post("/api/likes")
                        .param("userID", testUser.getUserID())
                        .param("recipeId", testRecipe.getRecipeId().toString()))
                .andExpect(status().isCreated());
    }

    @Test
    void testRemoveLike() throws Exception {
        Mockito.when(likeRepository.findByUserUserIDAndRecipeRecipeId(
                        testUser.getUserID(), testRecipe.getRecipeId()))
                .thenReturn(Optional.of(testLike));

        mockMvc.perform(delete("/api/likes")
                        .param("userID", testUser.getUserID())
                        .param("recipeId", testRecipe.getRecipeId().toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAddBookmark() throws Exception {
        mockMvc.perform(post("/api/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookmark)))
                .andExpect(status().isCreated());
    }

    @Test
    void testRemoveBookmark() throws Exception {
        Mockito.when(bookmarkRepository.findByUserUserIDAndRecipeRecipeId(
                        testUser.getUserID(), testRecipe.getRecipeId()))
                .thenReturn(Optional.of(testBookmark));

        mockMvc.perform(delete("/api/bookmarks/{userID}/{recipeId}",
                        testUser.getUserID(), testRecipe.getRecipeId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetUserRecipes() throws Exception {
        mockMvc.perform(get("/api/recipes/user/{userID}", testUser.getUserID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].title").value(testRecipe.getTitle()));
    }

    @Test
    void testGetUserComments() throws Exception {
        mockMvc.perform(get("/api/comments/user/{userID}", testUser.getUserID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].content").value(testComment.getContent()));
    }

    @Test
    void testGetUserBookmarks() throws Exception {
        mockMvc.perform(get("/api/bookmarks/{userID}", testUser.getUserID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void testIncrementViewCount() throws Exception {
        Mockito.when(viewRepository.findByRecipeRecipeId(testRecipe.getRecipeId()))
                .thenReturn(Optional.of(testView));

        mockMvc.perform(post("/api/views/{recipeId}", testRecipe.getRecipeId()))
                .andExpect(status().isOk())
                .andExpect(content().string("조회수가 증가했습니다."));
    }

//    @Test
//    void testGetUserLevel() throws Exception {
//        mockMvc.perform(get("/api/users/{userId}/grade", testUser.getUserId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userID").value(testUser.getGrade().getUserID()))
//                .andExpect(jsonPath("$.grade").value(testUser.getGrade().getGrade().toString()))
//                .andExpect(jsonPath("$.postCount").value(testUser.getGrade().getPostCount()))
//                .andExpect(jsonPath("$.commentCount").value(testUser.getGrade().getCommentCount()));
//    }

    @Test
    void testGetMyPageInfo() throws Exception {
        mockMvc.perform(get("/api/users/{userID}/mypage", testUser.getUserID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(testUser.getUserID()))
                .andExpect(jsonPath("$.userName").value(testUser.getUserName()))
                .andExpect(jsonPath("$.email").value(testUser.getUserEmail()));
    }
}
