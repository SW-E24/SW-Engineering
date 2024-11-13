package com.example.recipe.service;

import com.example.recipe.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // 회원에 대한 탐색은 Member~ 에서 진행하기 때문에 필요
    private final MemberRepository memberRepository;
    @Autowired
    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원가입 로직에 쓰일 중복 체크
    public boolean isDuplicateUser(String userID) {
        return memberRepository.existsByUserID(userID);
    }

    public boolean isDuplicateEmail(String userEmail) {
        return memberRepository.existsByUserEmail(userEmail);
    }

    public boolean isDuplicatePhone(String userPhone) {
        return memberRepository.existsByUserPhone(userPhone);
    }
}
