package com.zerobase.yeyak.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zerobase.yeyak.entity.Reservation;
import com.zerobase.yeyak.type.Approval;

@Repository
public interface ReservRepo extends JpaRepository<Reservation, Long> {
    // 사용자아이디로 조회(fk)
    List<Reservation> findAllByUser_Id(String id);

    // 상점 번호로 조회(fk)
    List<Reservation> findAllByStore_StoreNo(Long storeNo);

    // 상점번호 여러개로 조회
    List<Reservation> findAllByStore_StoreNoInOrderByReservDtAsc(List<Long> stores);

    // 방문확인용. 사용자아이디+상점번호로 조회-승인된것만
    List<Reservation> findAllByUser_IdAndStore_StoreNoAndApproval(String id,
								  Long storeNo,
								  Approval approval);

    // 리뷰 있는 예약만 가게번호로조회
    List<Reservation> findAllByStore_StoreNoAndReviewNotNull(Long storeNo);
}
