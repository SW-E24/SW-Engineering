package com.example.recipe.member;

/*
* 회원 데이터가 저장될 저장소가 가져야 할 '기능'
* - 회원 저장하기
* - 회원 탐색하기
*/


public interface MemberRepository {
    void save(Member member);   //회원 저장하기
    Member findById(String memberID);   //회원 검색하기
}
