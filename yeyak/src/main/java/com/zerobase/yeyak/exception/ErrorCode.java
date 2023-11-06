package com.zerobase.yeyak.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
//URL 입력돼있지 않은 코드는 이전페이지로, 입력돼있는 코드는 해당페이지로 redirect
public enum ErrorCode {
    // 로그인 실패
    NO_SUCH_USER("해당 유저가 없습니다. 아이디와 비밀번호를 확인해주세요", ""),
    // 키오스크 본인확인 실패
    NO_SUCH_USER_KIOSK("해당 유저가 없습니다. 사용자명과 핸드폰 번호를 확인해주세요", ""),
    // 세션없거나 만료시 메인화면(로그인화면)으로 튕김
    SESSSION_EXPIRED("로그인 세션이 만료되었거나 로그인하지 않았습니다. 다시 로그인해주세요.", "/"),
    // 권한 불일치
    ROLE_MISMATCH("이 계정으로는 접근할 수 없는 페이지 입니다", "/"),
    // 매장 찾기 실패
    NO_SUCH_STORE("해당 상점이 없습니다. 다시 시도해주세요", ""),
    // 예약 찾기 실패
    NO_SUCH_RESERVATION("해당 예약이 없습니다. 다시 시도해주세요", ""),
    // 예약 시간 아님
    NOT_RESERVED_TIME("예약시간이 지났거나 아직 예약시간이 아닙니다. 다시확인해주세요", "");

    private final String message;
    private final String url;
}
