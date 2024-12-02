package com.example.recipe.controller;

import com.example.recipe.dto.RegisterRequest;
import com.example.recipe.entity.Grade;
import com.example.recipe.entity.GradeType;
import com.example.recipe.entity.Member;
import com.example.recipe.repository.MemberRepository;
import com.example.recipe.service.AuthService;
import com.example.recipe.service.GradeService;
import com.example.recipe.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
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
    public ResponseEntity<Boolean> checkDuplicateId(@RequestParam String userId) {
        boolean isDuplicate = authService.isDuplicateUser(userId);
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
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest, RedirectAttributes redirectAttributes) {

        //중복 아이디 확인
        if (authService.isDuplicateUser(registerRequest.getUserId())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 아이디입니다.");
        }
        // 이메일 중복 확인
        if (authService.isDuplicateEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 이메일입니다.");
        }
        // 전화번호 중복 확인
        if (authService.isDuplicatePhone(registerRequest.getPhone())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 전화번호입니다.");
        }


        // 비밀번호 확인
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmuserPW())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다!-controller");
        }

        // 폼에 입력된 정보로 멤버 객체 저장
        Member newMember = new Member(
                registerRequest.getUserId(),
                registerRequest.getPassword(),
                registerRequest.getUserName(),
                registerRequest.getEmail(),
                registerRequest.getPhone()
        );
        memberService.saveMember(newMember);
        // 등급은 BASIC 으로 시작
        Grade newGrade = new Grade(registerRequest.getUserId(), GradeType.BASIC, 0, 0);
        gradeService.saveGrade(newGrade);

        return ResponseEntity.ok("회원가입 성공! 로그인 화면으로 이동합니다.");
    }


    /********************
     * 로그인 로직 처리
     * ******************/
//    @PostMapping("/login")
//    public String login(@RequestParam String userId, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
//        Member member = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));
//
//        if (member.getPassword().equals(password)) {
//            session.setAttribute("currentUser", member); // 세션에 사용자 정보 저장
//            return "redirect:/pages/mypage"; // 로그인 성공 시 mypage 페이지로 이동
//        } else {
//            redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 비밀번호가 틀립니다."); // 로그인 실패 시 메시지 출력
//            return "redirect:/pages/login"; // 로그인 실패 시 다시 로그인 페이지로 이동
//        }
//    }


    @PostMapping("/login")
    public String login(@RequestParam String userId, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Member member = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));

            if (member.getPassword().equals(password)) {
                session.setAttribute("currentUser", member); // 세션에 사용자 정보 저장
                System.out.println("로그인된 사용자: " + member.getUserId()); // 로그 출력 (확인용->잘나옴!)
                return "redirect:/"; // 로그인 성공 시 index 로 이동 (수정)
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 비밀번호가 틀립니다.");
                return "redirect:/pages/login"; // 로그인 실패 시 다시 로그인 페이지로 이동
            }
        } catch (RuntimeException e) {
            // 비정상적인 예외 처리를 위해... 필요없으면 제거해도 될 듯?
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/pages/login";
        }
    }

    /********************
     * 로그아웃 로직 처리
     * ******************/
//    @PostMapping("/logout")
//    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
//        //세션에서 사용자 정보 제거
//        session.invalidate();
//
//        redirectAttributes.addFlashAttribute("logoutMessage", "로그아웃 되었습니다");
//        return "redirect:/pages/login";
//    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            // 세션 무효화
            if (session != null) {
                session.invalidate();
                log.info("Session invalidated successfully.");
            } else {
                log.warn("Session was already null.");
            }
        } catch (IllegalStateException e) {
            log.warn("Session was already invalidated: ", e);
        } catch (Exception e) {
            log.error("Unexpected error during logout: ", e);
            return "error"; // 에러 페이지로 이동
        }

        // 로그아웃 메시지 추가
        if (redirectAttributes != null) {
            redirectAttributes.addFlashAttribute("logoutMessage", "로그아웃 되었습니다");
        } else {
            log.warn("RedirectAttributes is null.");
        }

        return "redirect:/pages/login"; // 로그인 페이지로 리다이렉트
    }

    @GetMapping("/logout")
    public String handleInvalidLogoutMethod() {
        log.warn("GET method is not supported for /logout. Redirecting to login.");
        return "redirect:/pages/login";
    }

    /************************
    * 현재 로그인된 사용자 가져오기
    * ***********************/
    @GetMapping("/currentUser")
    public ResponseEntity<Member> getCurrentUser(HttpSession session) {
        Member currentUser = (Member) session.getAttribute("currentUser");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(currentUser);
    }


}
