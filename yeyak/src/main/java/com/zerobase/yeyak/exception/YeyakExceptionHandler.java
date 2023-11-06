package com.zerobase.yeyak.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//에러 발생시 에러 코드와 메시지를 보여주고 이전 url로 돌아감
@ControllerAdvice
public class YeyakExceptionHandler {
    @ExceptionHandler(YeyakException.class)
    protected String handleCustomException(YeyakException e, Model model) {
	model.addAttribute("message", "ERROR CODE: "+e.getErrorCode() + "\n" + e.getMessage());
	model.addAttribute("url", e.getUrl());
	return "common/alertPage";
    }
}
