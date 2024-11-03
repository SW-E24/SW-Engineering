package com.example.recipe.member;

import jakarta.transaction.Transactional;
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

    /**********************************************************
    * 사용자 등급 업데이트 로직 - 게시글 수, 댓글 수가 변경될때마다 호출
    * **********************************************************/

    @Transactional
    public void updateMemberGreade(Member member) {
        Grade newGrade = member.getGrade();

        if (member.getPostCount() >= 50 && member.getCommentCount() >= 50) {
            newGrade = Grade.ONE_PLUS_PLUS;
        } else if (member.getPostCount() >= 30 && member.getCommentCount() >= 30) {
            newGrade = Grade.ONE_PLUS;
        } else if (member.getPostCount() >= 10 && member.getCommentCount() >= 10) {
            newGrade = Grade.ONE;
        } else {
            newGrade = Grade.BASIC;
        }

        //등급이 변경되었으면 새로 저장하기
        if (!newGrade.equals(member.getGrade())) {
            member.setGrade(newGrade);
            memberRepository.save(member);
        }

    }

    //게시글 수 증가
    @Transactional
    public void increasePostCount(Member member) {
        member.setCommentCount(member.getCommentCount() + 1);
        updateMemberGreade(member);
    }

    //댓글 수 증가
    @Transactional
    public void increaseCommentCount(Member member) {
        member.setCommentCount(member.getCommentCount() + 1);
        updateMemberGreade(member);
    }
}
