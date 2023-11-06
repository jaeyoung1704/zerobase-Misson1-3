package com.zerobase.yeyak.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerobase.yeyak.entity.Store;

@Repository
public interface StoreRepo extends JpaRepository<Store, Long> {

    // 상점 번호로 찾기
    Optional<Store> findById(Long storeNo);

    // 상점이름중 일부로 검색
    List<Store> findByNameContains(String name);

    // 매장주인 아이디로 검색
    List<Store> findAllByUser_Id(String id);
}
