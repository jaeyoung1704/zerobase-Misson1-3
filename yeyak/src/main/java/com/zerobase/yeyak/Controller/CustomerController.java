package com.zerobase.yeyak.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zerobase.yeyak.dto.LoginUser;
import com.zerobase.yeyak.dto.ReservationDto;
import com.zerobase.yeyak.dto.StoreDetail;
import com.zerobase.yeyak.dto.StoreSimple;
import com.zerobase.yeyak.dto.UserDto;
import com.zerobase.yeyak.entity.Store;
import com.zerobase.yeyak.exception.ErrorCode;
import com.zerobase.yeyak.exception.YeyakException;
import com.zerobase.yeyak.repo.ReservRepo;
import com.zerobase.yeyak.service.ReservService;
import com.zerobase.yeyak.service.StoreService;
import com.zerobase.yeyak.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller()
@RequestMapping("/customer/*")
@RequiredArgsConstructor
public class CustomerController {
    private final UserService userService;
    private final StoreService storeService;
    private final ReservService reservService;
    private final ReservRepo reservRepo;

    @GetMapping(value = "/")
    public String customerHome(HttpServletRequest request) {
	return "customer/main";
    }

    // 검색어가 없으면 전체검색, 있으면 상점명에 검색어 포함으로 검색
    @GetMapping("/store")
    public String storeSearch(Model model,
			      @RequestParam(required = false) String name) {
	List<StoreSimple> storeList = null;
	System.out.println("name:" + name);
	if (name == null || name.isBlank()) {
	    storeList = storeService.findAll();
	    model.addAttribute("storeList", storeList);
	    System.out.println(storeList);
	    return "customer/storeList";
	}
	// 공백제거
	name.replaceAll(" ", "");
	storeList = storeService.findLike(name);
	System.out.println(storeList);
	model.addAttribute("storeList", storeList);
	return "customer/storeList";
    }

    // 상점번호로 조회해서 model에 주입
    @GetMapping("/store/{storeNo}")
    public String storeDetail(@PathVariable Long storeNo, Model model,
			      @RequestParam(defaultValue = "1") Integer page) {
	System.out.println("page:" + page);
	StoreDetail store = storeService.findDetail(storeNo);
	model.addAttribute("store", store);

	// 리뷰목록도 조회해서 model에 주입
	List<ReservationDto> reviewList = reservService.reviewList(storeNo);
	model.addAttribute("reviewList", reviewList);
//	// 리뷰에 페이지 적용 (한페이지당 하드코딩으로 6개)
//	int len = reviewList.size();
//	int maxPage = (len - 1) / 6 + 1;
//	page = Math.min(page, maxPage);
//	reviewList = reviewList.subList(page * 6 - 6, Math.min(page * 6, len));
//	model.addAttribute("storeNo", storeNo);
//	model.addAttribute("page", page);
//	model.addAttribute("maxPage", maxPage);

	return "customer/storeDetail";
    }

    // 내 예약 조회
    @GetMapping("/reserv")
    public String reservList(HttpServletRequest request, Model model) {
	// 세션에서 로그인정보 가져오기
	LoginUser loginUser = userService.getLoginIdFromSession(request);
	// 가져온 아이디로 예약 조회
	List<ReservationDto> reservList =
	    reservService.reservListForUser(loginUser.getId());
	model.addAttribute("reservList", reservList);
	return "customer/reservList";
    }

    // 예약 등록
    @GetMapping("/reservDo")
    public String reservDo(HttpServletRequest request, Model model,
			   @RequestParam LocalDateTime reservDt,
			   @RequestParam String message,
			   @RequestParam Long storeNo) {
	// 세션에서 로그인정보 가져오기
	UserDto user = userService.getLoginUserFromSession(request);
	StoreSimple store = storeService.findSimple(storeNo);
	// 예약dto 생성후 entity로 변환해서 repo에 저장
	ReservationDto reservDto = ReservationDto.builder()
	    .user(user.toEntity())
	    .store(store.toEntity())
	    .reservDt(reservDt)
	    .message(message)
	    .build();
	reservRepo.save(reservDto.toEntity());
	// 가져온 아이디로 예약 등록
	model.addAttribute("message", "예약 등록 성공!");
	model.addAttribute("url", "/customer/reserv");
	return "common/alertPage";
    }

    // 내 예약 조회
    @PostMapping("/reservReview")
    public String reservReview(HttpServletRequest request, Model model,
			       @RequestParam Long reservNo,
			       @RequestParam String review) {
	reservService.reviewAdd(reservNo, review);
	return "redirect:/customer/reserv";
    }

}
