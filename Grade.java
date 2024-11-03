package com.example.recipe.member;

/*
* 회원 등급
* - 초기 가입한 회원은 BASIC 등급
* - (글 작성 10, 댓글 작성 10) 회원은 1등급
* - (글 작성 30, 댓글 작성 30) 회원은 1+등급
* - (글 작성 50, 댓글 작성 50) 회원은 1++등급
* */

public enum Grade {
    BASIC,
    ONE,
    ONE_PLUS,
    ONE_PLUS_PLUS
}
