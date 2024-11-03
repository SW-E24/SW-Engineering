package com.example.recipe.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/*
 * 회원을 저장할 로컬 저장소 -> sql 문법으로 데이터베이스에 저장
 * */

@Repository
public class MemoryMemberRepository implements MemberRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //Member 객체로부터 정보를 불러와서 sql 쿼리에 변수 적용 후 쿼리 요청으로 사용자 저장
    @Override
    public void save(Member member) {
        //신규 멤버인지 확인
        String checkSql = "SELECT COUNT(*) FROM MEMBERS WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, new Object[]{member.getId()}, Integer.class);

        if (count != null && count > 0) {
            // 기존 사용자가 있을 경우 UPDATE 수행
            String sql = "UPDATE MEMBERS SET password = ?, name = ?, email = ?, phone = ?, grade = ?, post_count = ?, comment_count = ? WHERE id = ?";
            jdbcTemplate.update(sql, member.getPassword(), member.getName(), member.getEmail(), member.getPhone(), member.getGrade().name(), member.getPostCount(), member.getCommentCount(), member.getId());
        } else {
            //MEMBERS 테이블에 신규 저장
            String sql = "INSERT INTO MEMBERS (id, password, name, email, phone, grade, post_count, comment_count) VALUES (?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(sql, member.getId(), member.getPassword(), member.getName(), member.getEmail(), member.getPhone(), member.getGrade().name(), member.getPostCount(), member.getCommentCount());

        }

    }

    @Override
    public Member findById(String memberID) {

        String sql = "SELECT * FROM MEMBERS WHERE id = ?";  //사용자 존재 여부 쿼리

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{memberID}, (rs, rowNum) -> {
                return new Member(
                        rs.getString("id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        Grade.valueOf(rs.getString("grade")),
                        rs.getInt("post_count"),
                        rs.getInt("comment_count")
                );
            });
        } catch (EmptyResultDataAccessException e) {    //사용자가 존재하지 않을 경우
            return null;
        }


    }
}
