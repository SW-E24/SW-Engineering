package com.example.recipe.service;

import com.example.recipe.entity.Grade;
import com.example.recipe.entity.Member;
import com.example.recipe.repository.MemberRepository;
import com.example.recipe.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@Service
public class MemberService {
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final GradeService gradeService;

    @Autowired
    public MemberService(MemberRepository memberRepository, GradeService gradeService) {
        this.memberRepository = memberRepository;
        this.gradeService = gradeService;
    }

    public Member findUserByUserID(String userId) {
        return memberRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
    }

    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    public void updateProfilePicture(String userId, MultipartFile file) throws IOException {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        member.setProfileImage(file.getBytes());
        memberRepository.save(member);
    }

    public Member getMemberById(String userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));
    }

    public Member getUserById(String userId) { //사용자 조회 메소드
        return memberRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public Member updateUser(String userId, Member updatedUser) {
        // 기존 사용자 정보 가져오기
        Member user = getUserById(userId);

        // 공백 처리: 비밀번호가 null이거나 공백이면 기존 비밀번호 유지
        if (updatedUser.getPassword() == null || updatedUser.getPassword().trim().isEmpty()) {
            updatedUser.setPassword(user.getPassword());
        }

        // 나머지 정보 업데이트
        user.setUserName(updatedUser.getUserName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        user.setPassword(updatedUser.getPassword());

        return memberRepository.save(user); // 저장
    }


    public Member getMyPageInfo(String userId) { //내 정보 보기
        return getUserById(userId); // 기존 메서드 활용
    }

    public Grade getUserLevel(String userId) {
        return gradeService.findByUserID(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User grade not found for id: " + userId));
    }

}
