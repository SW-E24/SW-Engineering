package com.example.recipe.controller;

import com.example.recipe.entity.Grade;
import com.example.recipe.entity.Member;
import com.example.recipe.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/{userId}/profile-picture")
    public ResponseEntity<String> uploadProfilePicture(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file) {
        try {
            memberService.updateProfilePicture(userId, file);
            return ResponseEntity.ok("프로필 사진이 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("프로필 사진 업데이트에 실패했습니다.");
        }
    }

    @GetMapping("/{userId}/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String userId) {
        Member member = memberService.getMemberById(userId);
        byte[] profileImage = member.getProfileImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(profileImage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Member> getUser(@PathVariable String userId) {
        Member user = memberService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/updateUser")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable String userId,
            @RequestBody Member updatedMember,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 기존 사용자 정보 가져오기
            Member existingMember = memberService.getUserById(userId);

            // 비밀번호 공백 확인 및 처리
            if (updatedMember.getPassword() == null || updatedMember.getPassword().trim().isEmpty()) {
                updatedMember.setPassword(existingMember.getPassword()); // 기존 비밀번호 유지
            }

            memberService.updateUser(userId, updatedMember);
            session.setAttribute("currentUser", updatedMember);

            response.put("success", true);
            response.put("redirectUrl", "/pages/mypage");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "회원 정보 수정 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{userId}/mypage")
    public ResponseEntity<Member> getMyPageInfo(@PathVariable String userId) {
        Member user = memberService.getMyPageInfo(userId);  // 마이페이지 보기 - 내 정보 보기 메소드 추가
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/grade")
    public ResponseEntity<Grade> getUserLevel(@PathVariable String userId) {
        Grade grade = memberService.getUserLevel(userId);  // 현재 등급 확인 메소드 추가
        return ResponseEntity.ok(grade);
    }

}
