package com.example.recipe.repository;

import com.example.recipe.entity.Grade;
import com.example.recipe.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, String> {
    Optional<Grade> findByUserID(String userID);
}
