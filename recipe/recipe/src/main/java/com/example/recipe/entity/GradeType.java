package com.example.recipe.entity;

/*
* 회원 등급
* - 초기 가입한 회원은 BASIC 등급
* - (글 작성 10, 댓글 작성 10) 회원은 1등급
* - (글 작성 30, 댓글 작성 30) 회원은 1+등급
* - (글 작성 50, 댓글 작성 50) 회원은 1++등급
*
* GradeType.java 는 단순히 등급 종류를 enum type 으로 나타내는 클래스이므로
* 회원의 등급과 관련한 로직은 Grade.java 에서 다룬다.
* 같은 파일 내에서 public 으로 작성해도 되지만, 보편적으로 한 파일에 하나씩 작성함
* */

public enum GradeType {
    BASIC,
    ONE,
    ONE_PLUS,
    ONE_PLUS_PLUS
}
