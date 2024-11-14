package com.example.recipe.repository;

import com.example.recipe.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByUserID(String userID);
    boolean existsByUserID(String userID);
    boolean existsByUserEmail(String userEmail);
    boolean existsByUserPhone(String userPhone);
}
