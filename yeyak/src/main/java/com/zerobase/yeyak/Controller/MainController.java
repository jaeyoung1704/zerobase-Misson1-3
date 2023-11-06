package com.zerobase.yeyak.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.cj.Session;
import com.zerobase.yeyak.dto.LoginUser;
import com.zerobase.yeyak.dto.UserDto;
import com.zerobase.yeyak.repo.UserRepo;
import com.zerobase.yeyak.service.UserService;
import com.zerobase.yeyak.type.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserRepo userRepo;
    private final UserService userService;

    // 메인페이지
    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
	System.out.println("home");
	// 로그인이 안돼있는경우에만 로그인 페이지 생성
	HttpSession session = request.getSession(true);
	if (session.getAttribute("loginUser") == null)
	    return "common/login";
	// 이미 로그인되어 있을경우 권한 별 메인페이지로 보내줌
	LoginUser loginUser = userService.getLoginIdFromSession(request);
	Role role = loginUser.getRole();
	// 역할별로 해당페이지로 보내줌
	if (role == Role.CUSTOMER)
	    return "redirect:/customer/";
	else if (role == Role.MANAGER)
	    return "redirect:/manager/";
	else
	    return "redirect:/kiosk/";

    }

    // 회원가입 페이지
    @GetMapping("/signUp")
    public String signUp(HttpServletRequest request) {
	return "common/signUp";
    }

    // 아이디 중복체크(AJAX)
    @ResponseBody
    @PostMapping("/checkId")
    public boolean checkId(String id) {
	return userService.dupleCheck(id);
    }

    // 회원가입 실행. 너무 단순하고 한번만 썼기 때문에 따로 서비스로 안뺐습니다.
    @PostMapping("/signUp")
    public String signUpDo(UserDto user) {
	userRepo.save(user.toEntity());
	return "redirect:/";
    }

    // 로그인 실행
    @PostMapping("/login")
    public String loginDo(@RequestParam String id, @RequestParam String pw,
			  HttpServletRequest request, Model model) {
	// 로그인 후 점장인지 매니저인지 키오스크인지만 가져옴
	Role role = userService.login(id, pw, request);
	// 실패시엔 exception handler가 알아서 에러페이지로 보내주므로 성공코드만 작성
	// 역할별로 해당페이지로 보내줌
	if (role == Role.CUSTOMER)
	    return "redirect:/customer/";
	else if (role == Role.MANAGER)
	    return "redirect:/manager/";
	else
	    return "redirect:/kiosk/";

    }

    // 로그아웃
    @GetMapping("/logout")
    public String logOut(HttpServletRequest request) {
	HttpSession session = request.getSession(false);
	session.removeAttribute("loginUser");
//	System.out.println(session.getAttribute("loginUser"));
	return "redirect:/";
    }
}
