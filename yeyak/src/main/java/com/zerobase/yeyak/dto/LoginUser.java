package com.zerobase.yeyak.dto;

import com.zerobase.yeyak.type.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

//로그인세션에 저장될 객체. read-only 이며 아이디와 유저타입만 존재
@Getter
@AllArgsConstructor
@ToString
public class LoginUser {
    private String id;
    private Role role;

}
