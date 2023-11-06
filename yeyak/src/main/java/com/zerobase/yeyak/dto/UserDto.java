package com.zerobase.yeyak.dto;

import com.zerobase.yeyak.entity.User;
import com.zerobase.yeyak.type.Role;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {
    private String id;
    private String pw;
    private Role role;
    private String name;
    private String phone;
    private String email;

    public User toEntity() {
	return User.builder()
	    .id(id)
	    .pw(pw)
	    .role(role)
	    .name(name)
	    .phone(phone)
	    .email(email)
	    .build();
    }
}
