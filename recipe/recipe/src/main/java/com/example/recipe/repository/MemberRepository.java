package com.example.recipe.repository;

/*
* 회원 데이터가 저장될 저장소가 가져야 할 '기능'
* - 회원 저장하기
* - 회원 탐색하기
*/


import com.example.recipe.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByUserID(String userID);
    boolean existsByUserID(String userID);
    boolean existsByUserEmail(String userEmail);
    boolean existsByUserPhone(String userPhone);}
