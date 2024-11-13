package com.example.recipe.service;

import com.example.recipe.entity.Grade;
import com.example.recipe.entity.GradeType;
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
    private MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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

    public Member updateUser(String userId, Member updatedUser) { //유저 정보 업데이트 메소드
        Member user = getUserById(userId);
        user.setUserName(updatedUser.getUserName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        return memberRepository.save(user);
    }

    public Member getMyPageInfo(String userId) { //내 정보 보기
        return getUserById(userId); // 기존 메서드 활용
    }

    public Grade getUserLevel(String userId) { //현재 등급 확인
        Member user = getUserById(userId);
        return user.getGrade();
    }

}

