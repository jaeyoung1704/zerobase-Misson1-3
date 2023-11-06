package com.zerobase.yeyak.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zerobase.yeyak.dto.LoginUser;
import com.zerobase.yeyak.dto.ReservationDto;
import com.zerobase.yeyak.dto.StoreDetail;
import com.zerobase.yeyak.dto.StoreSimple;
import com.zerobase.yeyak.entity.Reservation;
import com.zerobase.yeyak.entity.Store;
import com.zerobase.yeyak.entity.User;
import com.zerobase.yeyak.exception.ErrorCode;
import com.zerobase.yeyak.exception.YeyakException;
import com.zerobase.yeyak.repo.ReservRepo;
import com.zerobase.yeyak.repo.StoreRepo;
import com.zerobase.yeyak.repo.UserRepo;
import com.zerobase.yeyak.type.Approval;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservService {

    private final ReservRepo reservRepo;
    private final StoreRepo storeRepo;
    private final UserRepo userRepo;
    private final UserService userService;

    // 유저아이디로 조회해서 dto로 변환후 반환
    public List<ReservationDto> reservListForUser(String id) {
	return reservRepo.findAllByUser_Id(id)
	    .stream()
	    .map(x -> x.toDto())
	    .toList();
    }

    // 점장아이디로 조회해서 dto로 변환후 반환
    public List<ReservationDto> reservListForManager(String id) {
	// 우선 점장 아이디에 해당하는 상점들의 상점번호를 조회.
	List<Long> stores = storeRepo.findAllByUser_Id(id)
	    .stream()
	    .map(x -> x.getStoreNo())
	    .toList();
	// 해당 상점번호중 하나에 해당하는 예약 모두 조회, dto 변환후 반환
	return reservRepo.findAllByStore_StoreNoInOrderByReservDtAsc(stores)
	    .stream()
	    .map(x -> x.toDto())
	    .toList();
    }

    // 예약 승인 토글
    public void reservApproveToggle(Long reservNo, Approval approval) {
	Reservation reserv = reservRepo.findById(reservNo)
	    .orElseThrow(() -> new YeyakException(ErrorCode.NO_SUCH_RESERVATION));
	reserv.setApproval(approval);
	reservRepo.save(reserv);
    }

    // 키오스크용. 사용자 이름과 핸드폰 번호가 일치할경우 해당 사용자의 예약 조회
    public List<ReservationDto>
	   getReservListByUserNameAndPhoneAndStoreNo(String name, String phone,
						     HttpServletRequest request) {
	LoginUser loginUser = userService.getLoginIdFromSession(request);
	// 키오스크 아이디는 항상 "kio"+상점번호
	Long storeNo = Long.valueOf(loginUser.getId()
	    .substring(3));
	System.out.println("store no:" + storeNo);
	// 사용자명과 핸드폰번호가 일치하지않으면 에러 발생
	User user = userRepo.findByNameAndPhone(name, phone)
	    .orElseThrow(() -> new YeyakException(ErrorCode.NO_SUCH_USER_KIOSK));
	System.out.println(user);
	// 해당 사용자의 예약 리스트를 dto로 매핑해서 반환
	return reservRepo
	    .findAllByUser_IdAndStore_StoreNoAndApproval(user.getId(), storeNo,
		Approval.APPROVED)
	    .stream()
	    .map(x -> x.toDto())
	    .toList();
    }

    // 키오스크용. 방문처리
    public void visit(Long reservNo) {
	var now = LocalDateTime.now();
	// 예약번호로 예약정보 조회
	Reservation reserv = reservRepo.findById(reservNo)
	    .orElseThrow(() -> new YeyakException(ErrorCode.NO_SUCH_RESERVATION));

	// 예약시간이 아닌경우 에러처리
	if (reserv.getReservDt()
	    .isBefore(now.minusMinutes(10))
	    || reserv.getReservDt()
		.isAfter(now.plusMinutes(10)))
	    throw new YeyakException(ErrorCode.NOT_RESERVED_TIME);

	// 예약처리후 다시 저장
	reserv.setVisited(true);
	reservRepo.save(reserv);
    }

    public void reviewAdd(Long reservNo, String review) {
	// 예약번호로 예약정보 조회
	Reservation reserv = reservRepo.findById(reservNo)
	    .orElseThrow(() -> new YeyakException(ErrorCode.NO_SUCH_RESERVATION));
	// 리뷰 추가후 다시 저장
	reserv.setReview(review);
	reservRepo.save(reserv);
    }

    // 상점번호로 리뷰 조회
    public List<ReservationDto> reviewList(Long storeNo) {
	return reservRepo.findAllByStore_StoreNoAndReviewNotNull(storeNo)
	    .stream()
	    .map(x -> x.toDto())
	    .toList();
    }
}
