package com.example.jwt.repository;

import com.example.jwt.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<User>findOneWithAuthoritiesByUsername(String username);

    Optional<Object> findBySocialId(long socialId);
}
