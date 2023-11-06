package com.zerobase.yeyak.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerobase.yeyak.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    // 아이디로 검색
    Optional<User> findById(String id);

    // 아이디와 비밀번호가 일치하면 검색
    Optional<User> findByIdAndPw(String id, String pw);

    // 사용자 이름과 전화번호가 일치하면 검색
    Optional<User> findByNameAndPhone(String name, String phone);
}
