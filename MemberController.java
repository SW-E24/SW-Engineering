package com.example.recipe.controller;

import com.example.recipe.member.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * 회원 관리 요청을 처리하기 위한 컨트롤러
 * */


@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemoryMemberRepository memoryMemberRepository;

    /********************
    * 회원가입 로직 처리
    * ******************/

    @GetMapping("/check-duplicate-id")
    @ResponseBody
    public boolean checkDuplicateId(@RequestParam String userID) {
        Member existingMember = memoryMemberRepository.findById(userID);
        return existingMember == null;  //아이디 사용 가능하면 true 반환 (findById가 null이면 true인거지)
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String userID,
                           @RequestParam String userPW,
                           @RequestParam String confirmuserPW,
                           @RequestParam String userName,
                           @RequestParam String userEmail,
                           @RequestParam String userPhone,
                           RedirectAttributes redirectAttributes) {

        //중복 아이디 확인
        Member existingMember = memoryMemberRepository.findById(userID);
        if (existingMember != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미 사용 중인 아이디입니다.");
            return "redirect:/register";
        }

        //비밀번호 재확인
        if (!userPW.equals(confirmuserPW)) {
            redirectAttributes.addFlashAttribute("pwerrorMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/register";
        }

        //폼에 입력된 정보로 멤버 객체 생성 후 저장 (게시글 수는 default 값인 0)
        Member newMember = new Member(userID, userPW, userName, userEmail, userPhone, Grade.BASIC, 0, 0);
        memberService.saveMember(newMember);

        return "redirect:/";
    }


    /********************
     * 로그인 로직 처리
     * ******************/

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String userID, @RequestParam String userPW, HttpSession session, RedirectAttributes redirectAttributes) {
        Member member = memoryMemberRepository.findById(userID); // 사용자 조회

        if (member != null && member.getPassword().equals(userPW)) {
            session.setAttribute("currentUser", member); // 세션에 현재 사용자 정보 저장
            return "redirect:/myinfo";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 비밀번호가 틀립니다.");
            return "redirect:/login";
        }
    }


    /***********************
     * 내 정보 확인 로직 처리
     * ***********************/

    @GetMapping("/myinfo")
    public String showMyInfoForm(Model model, @SessionAttribute(name = "currentUser", required = false) Member currentUser) {
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            return "myinfo";
        } else {
            // 사용자 정보가 없으면 로그인 페이지로 리다이렉트
            return "redirect:/login";
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

    /**********************
    * 테스트용 버튼 로직 처리
    * *********************/

    //게시글 증가 버튼 누르면 호출
    @PostMapping("/increase-post-count")
    public String increasePostCount(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Member currentUser = (Member) session.getAttribute("currentUser");

            if (currentUser != null) {
                currentUser.setPostCount(currentUser.getPostCount() + 1);
                memberService.updateMemberGreade(currentUser);
                memoryMemberRepository.save(currentUser);
                redirectAttributes.addFlashAttribute("message", "게시글 수가 증가했습니다.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
            }

            return "redirect:/myinfo";
        } catch (Exception e) {
            // 로그 남기기
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 수 증가 중 오류가 발생했습니다.");
            return "redirect:/myinfo"; // 오류 발생 시에도 myinfo 페이지로 리다이렉트
        }
    }


    //댓글 증가 버튼 누르면 호출
    @PostMapping("/increase-comment-count")
    public String increaseCommentCount(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Member currentUser = (Member) session.getAttribute("currentUser");
            if (currentUser != null) {
                currentUser.setCommentCount(currentUser.getCommentCount() + 1);
                memberService.updateMemberGreade(currentUser); // 등급 업데이트
                memoryMemberRepository.save(currentUser);
                redirectAttributes.addFlashAttribute("message", "댓글 수가 증가했습니다.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
            }
            return "redirect:/myinfo"; // 리다이렉트
        } catch (Exception e) {
            // 로그 남기기
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 수 증가 중 오류가 발생했습니다.");
            return "redirect:/myinfo"; // 오류 발생 시에도 myinfo 페이지로 리다이렉트
        }
    }

}
