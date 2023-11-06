package com.zerobase.yeyak.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zerobase.yeyak.dto.LoginUser;
import com.zerobase.yeyak.dto.ReservationDto;
import com.zerobase.yeyak.dto.StoreDetail;
import com.zerobase.yeyak.dto.StoreSimple;
import com.zerobase.yeyak.dto.UserDto;
import com.zerobase.yeyak.entity.Store;
import com.zerobase.yeyak.repo.StoreRepo;
import com.zerobase.yeyak.repo.UserRepo;
import com.zerobase.yeyak.service.ReservService;
import com.zerobase.yeyak.service.StoreService;
import com.zerobase.yeyak.service.UserService;
import com.zerobase.yeyak.type.Approval;
import com.zerobase.yeyak.type.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller()
@RequestMapping("/manager/*")
@RequiredArgsConstructor
public class ManagerController {
    private final ReservService reservService;
    private final UserService userService;
    private final StoreService storeService;
    private final StoreRepo storeRepo;
    private final UserRepo userRepo;

    // 점장 홈
    @GetMapping("/")
    public String managerHome() {
	return "manager/main";
    }

    // 내 매장에 들어온 예약 목록 조회
    @GetMapping("/reserv")
    public String managerReserv(HttpServletRequest request, Model model) {
	// 세션에서 유저 아이디 받아옴
	LoginUser user = userService.getLoginIdFromSession(request);
	// 유저 아이디로 예약목록 조회
	List<ReservationDto> reservList =
	    reservService.reservListForManager(user.getId());
	model.addAttribute("reservList", reservList);
	return "manager/reservList";
    }

    // 예약 승인처리(토글)
    @PostMapping("/reserv/approve")
    public String managerReservApprove(Model model, @RequestParam Long reservNo,
				       @RequestParam Approval approval) {
	reservService.reservApproveToggle(reservNo, approval);
	return "redirect:/manager/reserv";
    }

    // 내 매장 조회
    @GetMapping("stores")
    public String managerStores(HttpServletRequest request, Model model) {
	// 세션에서 유저 아이디 받아옴
	LoginUser user = userService.getLoginIdFromSession(request);
	// 유저 아이디로 매장 조회
	List<StoreSimple> storeList = storeService.findByUserId(user.getId());
	model.addAttribute("storeList", storeList);
	return "manager/storeList";
    }

    // 매장 등록
    @GetMapping("storeReg")
    public String managerStoreReg(HttpServletRequest request, Model model) {
	return "manager/storeReg";
    }

    // 매장 등록 실행
    @Transactional
    @PostMapping("storeReg")
    public String managerStoreRegDo(HttpServletRequest request,
				    @RequestParam String name,
				    @RequestParam String description,
				    @RequestParam String address1,
				    @RequestParam String address2,
				    @RequestParam String pin, Model model) {
	// user 부분은 세션에서 가져오고 나머지는 form에서 가져옴
	UserDto User = userService.getLoginUserFromSession(request);
	StoreDetail store = StoreDetail.builder()
	    .user(User)
	    .name(name)
	    .description(description)
	    .address1(address1)
	    .address2(address2)
	    .build();
	Store flushedStore = storeRepo.saveAndFlush(store.toEntity());
	// 매장 등록 성공시 자동으로 키오스크도 등록
	UserDto kiosk = UserDto.builder()
	    .id("kio" + flushedStore.getStoreNo()) // kio+가게번호로 자동 아이디 생성
	    .pw(pin)
	    .role(Role.KIOSK)
	    .build();
	userRepo.save(kiosk.toEntity());
	model.addAttribute("message",
	    "매장 등록 성공!\n 키오스크 아이디가 '" + kiosk.getId() + "' 로 생성되었습니다");
	model.addAttribute("url", "/manager/stores");
	return "common/alertPage";
    }
}
