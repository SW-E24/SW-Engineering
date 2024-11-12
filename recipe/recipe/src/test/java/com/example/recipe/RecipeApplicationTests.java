package com.example.recipe;

import com.example.recipe.entity.GradeType;
import com.example.recipe.entity.Member;
import com.example.recipe.repository.MemberRepository;
import com.example.recipe.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/***********************************************
 * 등급 제도 자동 수정 테스트를 위한 서비스 테스트 파일
 * **********************************************/


@SpringBootTest
class RecipeApplicationTests {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@BeforeEach
	void setUp() {
		// 초기 데이터 설정: test006 회원을 데이터베이스에 저장
		Member member = new Member("test006", "password", "Test User", "test006@example.com", "010-1234-5678", GradeType.ONE_PLUS, 20, 49);
		memberRepository.save(member);
	}

	@Test
	void testIncreaseCommentCount() {
		// 댓글 수 증가 메서드 호출
		Member member = memberRepository.findById("test006");
		memberService.increaseCommentCount(member);

		// 댓글 수 및 등급 확인
		Member updatedMember = memberRepository.findById("test006");

		assertThat(updatedMember).isNotNull();
		assertThat(updatedMember.getCommentCount()).isEqualTo(50);
		assertThat(updatedMember.getGrade()).isEqualTo(GradeType.ONE_PLUS_PLUS);  // 조건에 맞게 등급이 변경되었는지 확인
	}
}
