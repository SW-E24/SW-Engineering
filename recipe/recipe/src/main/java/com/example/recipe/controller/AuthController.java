package com.example.recipe.controller;

import com.example.recipe.entity.Grade;
import com.example.recipe.entity.GradeType;
import com.example.recipe.entity.Member;
import com.example.recipe.repository.MemberRepository;
import com.example.recipe.service.AuthService;
import com.example.recipe.service.GradeService;
import com.example.recipe.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * 회원 관리 요청을 처리하기 위한 컨트롤러
 * - 회원가입
 * - 로그인
 * - 로그아웃
 * */


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private GradeService gradeService;


    /********************
    * 회원가입 로직 처리
    * ******************/
    // 아이디 중복 체크
    @GetMapping("/check-duplicate-id")
    public ResponseEntity<Boolean> checkDuplicateId(@RequestParam String userID) {
        boolean isDuplicate = authService.isDuplicateUser(userID);
        return ResponseEntity.ok(isDuplicate);
    }

    // 이메일 중복 체크
    @GetMapping("/check-duplicate-email")
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam String userEmail) {
        // 이메일 중복 여부 확인
        boolean isDuplicate = authService.isDuplicateEmail(userEmail);
        return ResponseEntity.ok(isDuplicate);
    }

    // 전화번호 중복 체크
    @GetMapping("/check-duplicate-phone")
    public ResponseEntity<Boolean> checkDuplicatePhone(@RequestParam String userPhone) {
        // 전화번호 중복 여부 확인
        boolean isDuplicate = authService.isDuplicatePhone(userPhone);
        return ResponseEntity.ok(isDuplicate);
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Member newMember, @RequestParam String confirmuserPW,RedirectAttributes redirectAttributes) {

        //중복 아이디 확인
        if (authService.isDuplicateUser(newMember.getUserID())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 아이디입니다.");
        }
        // 이메일 중복 확인
        if (authService.isDuplicateEmail(newMember.getUserEmail())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 이메일입니다.");
        }
        // 전화번호 중복 확인
        if (authService.isDuplicatePhone(newMember.getUserPhone())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 전화번호입니다.");
        }


        // 비밀번호 확인
        if (!newMember.getUserPW().equals(confirmuserPW)) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        // 폼에 입력된 정보로 멤버 객체 저장
        memberService.saveMember(newMember);
        // 등급은 BASIC 으로 시작
        Grade newGrade = new Grade(newMember.getUserID(), GradeType.BASIC, 0, 0);
        gradeService.saveGrade(newGrade);

        return ResponseEntity.ok("회원가입 성공! 로그인 화면으로 이동합니다.");
    }


    /********************
     * 로그인 로직 처리
     * ******************/
    @PostMapping("/login")
    public String login(@RequestParam String userID, @RequestParam String userPW, HttpSession session, RedirectAttributes redirectAttributes) {
        Member member = memberRepository.findById(userID).orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));

        if (member.getUserPW().equals(userPW)) {
            session.setAttribute("currentUser", member); // 세션에 사용자 정보 저장
            return "redirect:/mypage"; // 로그인 성공 시 mypage 페이지로 이동
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 비밀번호가 틀립니다."); // 로그인 실패 시 메시지 출력
            return "redirect:/login"; // 로그인 실패 시 다시 로그인 페이지로 이동
        }
    }


    /********************
     * 로그아웃 로직 처리
     * ******************/
    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        //세션에서 사용자 정보 제거
        session.invalidate();

        redirectAttributes.addFlashAttribute("logoutMessage", "로그아웃 되었습니다");
        return "redirect:/login";
    }

}
