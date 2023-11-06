package com.zerobase.yeyak.service;

import java.net.http.HttpRequest;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zerobase.yeyak.dto.UserDto;
import com.zerobase.yeyak.dto.LoginUser;
import com.zerobase.yeyak.entity.User;
import com.zerobase.yeyak.exception.ErrorCode;
import com.zerobase.yeyak.exception.YeyakException;
import com.zerobase.yeyak.repo.UserRepo;
import com.zerobase.yeyak.type.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    // 아이디 중복체크
    public boolean dupleCheck(String id) {
	Optional<User> user = userRepo.findById(id);
	if (user.isPresent())
	    return false;
	return true;
    }

    // 로그인
    public Role login(String id, String pw, HttpServletRequest request) {
	// 로그인 실패시 에러 throw
	User user = userRepo.findByIdAndPw(id, pw)
	    .orElseThrow(() -> new YeyakException(ErrorCode.NO_SUCH_USER));
	// 세션이 있으면 가져오고 없으면 새로 생성
	HttpSession session = request.getSession(true);
	// 세션 30분간 유지
	session.setMaxInactiveInterval(1800);
	// 로그인 세션 생성
	session.setAttribute("loginUser", new LoginUser(id, user.getRole()));
	return user.getRole();
    }

    // 세션에서 로그인 정보(아이디,권한만) 가져오기
    public LoginUser getLoginIdFromSession(HttpServletRequest request) {
	// 세션이 있으면 가져오고 없으면 세션만료 에러페이지
	HttpSession session = request.getSession(false);
	if (session == null)
	    throw new YeyakException(ErrorCode.SESSSION_EXPIRED);
	LoginUser loginId = (LoginUser) session.getAttribute("loginUser");
	// 세션 있어도 로그인안돼있으면 에러페이지
	if (loginId == null)
	    throw new YeyakException(ErrorCode.SESSSION_EXPIRED);
	return loginId;
    }

    // 세션에서 로그인유저 모든 정보 가져오기
    public UserDto getLoginUserFromSession(HttpServletRequest request) {
	// id로 유저repo에서 검색
	LoginUser loginId = getLoginIdFromSession(request);
	User user = userRepo.findById(loginId.getId())
	    .orElseThrow(() -> new YeyakException(ErrorCode.NO_SUCH_USER));
	return user.toDto();
    }

}
