package com.zerobase.yeyak.exception;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class YeyakException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;
    private String url;

    public YeyakException(ErrorCode errorCode) {
	HttpServletRequest request =
	    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
		.getRequest();
	this.errorCode = errorCode;
	this.message = errorCode.getMessage();

	// 에러코드에서 지정한 url이 있을경우 해당 url로 이동.
	// 없을경우 포스트방식이면 이전페이지로 돌아가고, 겟방식이면 현재페이지에서 알림창만 띄워줌
	this.url = !errorCode.getUrl()
	    .isBlank() ? errorCode.getUrl()
	    : request.getMethod()
		.equals("POST") ? request.getHeader("Referer")
	    : request.getRequestURI();

    }

}
