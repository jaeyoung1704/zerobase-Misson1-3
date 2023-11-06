package com.zerobase.yeyak.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zerobase.yeyak.dto.ReservationDto;
import com.zerobase.yeyak.dto.StoreDetail;
import com.zerobase.yeyak.dto.StoreSimple;
import com.zerobase.yeyak.entity.Store;
import com.zerobase.yeyak.exception.ErrorCode;
import com.zerobase.yeyak.exception.YeyakException;
import com.zerobase.yeyak.repo.StoreRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepo storeRepo;

    // 매장 전체 조회- repo에서 조회후 dto로 변환해서 반환
    public List<StoreSimple> findAll() {
	return storeRepo.findAll()
	    .stream()
	    .map(x -> x.toSimple())
	    .toList();
    }

    // 매장 검색어 조회- repo에서 조회후 dto로 변환해서 반환
    public List<StoreSimple> findLike(String name) {
	return storeRepo.findByNameContains(name)
	    .stream()
	    .map(x -> x.toSimple())
	    .toList();
    }

    // 상점 상세 조회
    public StoreDetail findDetail(Long storeNo) {
	// 해당번호의 상점이 없으면 에러발생
	Store store = storeRepo.findById(storeNo)
	    .orElseThrow(() -> new YeyakException(ErrorCode.NO_SUCH_STORE));
	return store.toDetail();
    }

    // 상점 번호로 간단 조회
    public StoreSimple findSimple(Long storeNo) {
	// 해당번호의 상점이 없으면 에러발생
	Store store = storeRepo.findById(storeNo)
	    .orElseThrow(() -> new YeyakException(ErrorCode.NO_SUCH_STORE));
	return store.toSimple();
    }

    // 유저 아이디로 조회
    public List<StoreSimple> findByUserId(String userId) {
	return storeRepo.findAllByUser_Id(userId)
	    .stream()
	    .map(x -> x.toSimple())
	    .toList();
    }

}
