package com.example.recipe.service;

import com.example.recipe.entity.Grade;
import com.example.recipe.entity.GradeType;
import com.example.recipe.repository.GradeRepository;
import com.example.recipe.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**************************************
 * 사용자 등급 관리 로직
 * - 게시글 등록 시 count 증가 메소드
 * - 게시글 삭제 시 count 감소 메소드
 * - 댓글 등록 시 count 증가 메소드
 * - 댓글 삭제 시 count 감소 메소드
 * - 사용자 count 에 따른 등급 갱신 트랜잭션
 * ***********************************/
@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    public void saveGrade(Grade grade) {
        gradeRepository.save(grade);
    }

    public void increasePostCount(String userID) {
        Grade grade = gradeRepository.findByUserID(userID)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        grade.setPostCount(grade.getPostCount() + 1);
        gradeRepository.save(grade);
    }

    public void decreasePostCount(String userID) {
        Grade grade = gradeRepository.findByUserID(userID)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        if (grade.getPostCount() > 0) {
            grade.setPostCount(grade.getPostCount() - 1);
            gradeRepository.save(grade);
        } else {
            throw new IllegalStateException("postCount는 음수일 수 없습니다.");
        }
    }

    public void increaseCommentCount(String userID) {
        Grade grade = gradeRepository.findByUserID(userID)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        grade.setCommentCount(grade.getCommentCount() + 1);
        gradeRepository.save(grade);
    }

    public void decreaseCommentCount(String userID) {
        Grade grade = gradeRepository.findByUserID(userID)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        if (grade.getPostCount() > 0) {
            grade.setCommentCount(grade.getCommentCount() - 1);
            gradeRepository.save(grade);
        } else {
            throw new IllegalStateException("commentCount는 음수일 수 없습니다.");
        }
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
