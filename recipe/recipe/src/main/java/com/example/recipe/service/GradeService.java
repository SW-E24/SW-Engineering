package com.example.recipe.service;

import com.example.recipe.entity.Grade;
import com.example.recipe.entity.GradeType;
import com.example.recipe.repository.GradeRepository;
import com.example.recipe.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**********************************************************
 * 사용자 등급 업데이트 로직 - 게시글 수, 댓글 수가 변경될때마다 호출
 * **********************************************************/
@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    public void saveGrade(Grade grade) {
        gradeRepository.save(grade);
    }

    @Transactional
    public void updateMemberGreade(String userID) {
        // 회원 아이디로 회원의 Grade 조회
        Grade grade = gradeRepository.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

        // 게시글 수와 댓글 수에 따라 등급 갱신
        GradeType newGradeType = grade.getGrade();

        if (grade.getPostCount() >= 50 && grade.getCommentCount() >= 50) {
            newGradeType = GradeType.ONE_PLUS_PLUS;
        } else if (grade.getPostCount() >= 30 && grade.getCommentCount() >= 30) {
            newGradeType = GradeType.ONE_PLUS;
        } else if (grade.getPostCount() >= 10 && grade.getCommentCount() >= 10) {
            newGradeType = GradeType.ONE;
        } else {
            newGradeType = GradeType.BASIC;
        }

        //등급이 변경되었으면 새로 저장하기
        if (!newGradeType.equals(grade.getGrade())) {
            grade.setGrade(newGradeType);
            gradeRepository.save(grade);
        }

    }
}
