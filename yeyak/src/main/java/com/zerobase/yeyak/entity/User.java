package com.zerobase.yeyak.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.zerobase.yeyak.dto.UserDto;
import com.zerobase.yeyak.type.Role;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
    @Id
    private String id;
    private String pw;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private String name;
    private String phone;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime regDt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime upDt;

    public UserDto toDto() {
	return UserDto.builder()
	    .id(id)
	    .pw(pw)
	    .role(role)
	    .name(name)
	    .email(email)
	    .phone(phone)
	    .build();
    }
}
