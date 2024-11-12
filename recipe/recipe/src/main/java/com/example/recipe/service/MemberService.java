package com.example.recipe.service;

import com.example.recipe.entity.Member;
import com.example.recipe.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void saveMember(Member member) {
        memberRepository.save(member);
    }

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
