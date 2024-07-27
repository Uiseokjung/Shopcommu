package com.backend.shopcommu.domain.user.repository;

import com.backend.shopcommu.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자 이름으로 사용자 검색
    Optional<User> findByUsername(String username);

    // 이메일로 사용자 검색
    Optional<User> findByEmail(String email);

    // 닉네임으로 사용자 검색
    Optional<User> findByNickname(String nickname);
}
