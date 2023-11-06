package com.zerobase.yeyak.intercepter;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zerobase.yeyak.dto.LoginUser;
import com.zerobase.yeyak.exception.ErrorCode;
import com.zerobase.yeyak.exception.YeyakException;
import com.zerobase.yeyak.type.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//세션이 없거나 만료시 로그인 화면으로 인터셉트(예외 핸들러 사용)
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
			   Object handler, ModelAndView modelAndView)
	throws Exception {
	HttpSession session = request.getSession(false);
	// 세션이 없으면 로그인 화면으로
	if (session == null)
	    throw new YeyakException(ErrorCode.SESSSION_EXPIRED);

	// 로그인이 안돼있으면 에러 던짐
	LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
	if (loginUser == null)
	    throw new YeyakException(ErrorCode.SESSSION_EXPIRED);

	// 페이지에 필요한 권한 확인(*/customer/*=CUSTOMER, */manager/*=MANAGER,
	// */kiosk/*=KIOSK)
	Role needRole = Role.valueOf(request.getRequestURL()
	    .toString()
	    .split("/")[3].toUpperCase());
	System.out.println(needRole);
	if (loginUser.getRole() != needRole)
	    throw new YeyakException(ErrorCode.ROLE_MISMATCH);

	HandlerInterceptor.super.postHandle(request, response, handler,
	    modelAndView);
    }
}
